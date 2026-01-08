package com.example.emaildemo.service;

public interface EmailService {
    
    void sendEmail(String to, String subject, String body) throws Exception;
    
    String getProviderName();
}
