package com.example.emaildemo.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("sendGridEmailService")
@Slf4j
public class SendGridEmailService implements EmailService {
    
    @Value("${sendgrid.api.key}")
    private String apiKey;
    
    @Value("${sendgrid.from.email}")
    private String fromEmail;
    
    @Value("${sendgrid.from.name}")
    private String fromName;
    
    @Override
    public void sendEmail(String to, String subject, String body) throws Exception {
        log.info("Sending email via SendGrid to: {}", to);
        
        try {
            Email from = new Email(fromEmail, fromName);
            Email toEmail = new Email(to);
            Content content = new Content("text/html", body);
            Mail mail = new Mail(from, subject, toEmail, content);
            
            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();
            
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("Email sent successfully via SendGrid. Status: {}", response.getStatusCode());
            } else {
                log.error("SendGrid returned status: {} - {}", response.getStatusCode(), response.getBody());
                throw new Exception("SendGrid returned error status: " + response.getStatusCode());
            }
            
        } catch (IOException ex) {
            log.error("Error sending email via SendGrid: {}", ex.getMessage());
            throw new Exception("Failed to send email via SendGrid: " + ex.getMessage());
        }
    }
    
    @Override
    public String getProviderName() {
        return "sendgrid";
    }
}
