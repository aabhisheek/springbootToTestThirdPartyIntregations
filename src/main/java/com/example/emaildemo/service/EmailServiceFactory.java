package com.example.emaildemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceFactory {
    
    private final Map<String, EmailService> emailServices;
    
    @Value("${email.provider:sendgrid}")
    private String defaultProvider;
    
    public EmailService getEmailService(String provider) {
        if (provider == null || provider.trim().isEmpty()) {
            provider = defaultProvider;
        }
        
        EmailService emailService;
        if ("ses".equalsIgnoreCase(provider)) {
            emailService = emailServices.get("awsSesEmailService");
        } else if ("sendgrid".equalsIgnoreCase(provider)) {
            emailService = emailServices.get("sendGridEmailService");
        } else {
            throw new IllegalArgumentException("Unknown email provider: " + provider);
        }
        
        if (emailService == null) {
            throw new IllegalStateException("Email service not found for provider: " + provider);
        }
        
        return emailService;
    }
    
    public EmailService getDefaultEmailService() {
        return getEmailService(defaultProvider);
    }
}
