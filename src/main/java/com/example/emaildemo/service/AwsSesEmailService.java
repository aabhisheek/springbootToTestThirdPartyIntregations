package com.example.emaildemo.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.example.emaildemo.dto.EmailAttachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Properties;

@Service("awsSesEmailService")
@Slf4j
public class AwsSesEmailService implements EmailService {
    
    @Value("${aws.ses.region:us-east-1}")
    private String region;
    
    @Value("${aws.ses.access-key}")
    private String accessKey;
    
    @Value("${aws.ses.secret-key}")
    private String secretKey;
    
    @Value("${aws.ses.from-email}")
    private String fromEmail;
    
    @Override
    public void sendEmail(String to, String subject, String body) throws Exception {
        log.info("Sending email via AWS SES to: {}", to);
        
        try {
            // Create credentials
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            
            // Build SES client
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withRegion(Regions.fromName(region))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();
            
            // Create email request
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(new com.amazonaws.services.simpleemail.model.Message()
                            .withBody(new Body()
                                    .withHtml(new Content().withCharset("UTF-8").withData(body))
                                    .withText(new Content().withCharset("UTF-8").withData(body)))
                            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                    .withSource(fromEmail);
            
            // Send email
            SendEmailResult result = client.sendEmail(request);
            log.info("Email sent successfully via AWS SES. Message ID: {}", result.getMessageId());
            
        } catch (Exception ex) {
            log.error("Error sending email via AWS SES: {}", ex.getMessage());
            throw new Exception("Failed to send email via AWS SES: " + ex.getMessage());
        }
    }
    
    @Override
    public void sendEmailWithAttachments(String to, String subject, String body, List<EmailAttachment> attachments) throws Exception {
        log.info("Sending email with {} attachment(s) via AWS SES to: {}", 
                attachments != null ? attachments.size() : 0, to);
        
        try {
            // Create credentials
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            
            // Build SES client
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withRegion(Regions.fromName(region))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();
            
            // Create a MIME message for attachments
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            
            // Set email headers
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            
            // Create multipart message
            MimeMultipart multipart = new MimeMultipart();
            
            // Add body part
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(body, "text/html; charset=UTF-8");
            multipart.addBodyPart(bodyPart);
            
            // Add attachments
            if (attachments != null && !attachments.isEmpty()) {
                for (EmailAttachment attachment : attachments) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    DataSource source = new ByteArrayDataSource(
                            attachment.getFileContent(), 
                            attachment.getContentType()
                    );
                    attachmentPart.setDataHandler(new DataHandler(source));
                    attachmentPart.setFileName(attachment.getFileName());
                    multipart.addBodyPart(attachmentPart);
                    log.info("Added attachment: {}", attachment.getFileName());
                }
            }
            
            // Set content
            message.setContent(multipart);
            
            // Send raw email
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
            
            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
            SendRawEmailResult result = client.sendRawEmail(rawEmailRequest);
            
            log.info("Email with attachments sent successfully via AWS SES. Message ID: {}", result.getMessageId());
            
        } catch (Exception ex) {
            log.error("Error sending email with attachments via AWS SES: {}", ex.getMessage());
            throw new Exception("Failed to send email with attachments via AWS SES: " + ex.getMessage());
        }
    }
    
    @Override
    public String getProviderName() {
        return "ses";
    }
}
