package com.example.emaildemo.service;

import com.example.emaildemo.dto.EmailAttachment;

import java.util.List;

public interface EmailService {
    
    void sendEmail(String to, String subject, String body) throws Exception;
    
    void sendEmailWithAttachments(String to, String subject, String body, List<EmailAttachment> attachments) throws Exception;
    
    String getProviderName();
}
