package com.example.emaildemo.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
                    .withMessage(new Message()
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
    public String getProviderName() {
        return "ses";
    }
}
