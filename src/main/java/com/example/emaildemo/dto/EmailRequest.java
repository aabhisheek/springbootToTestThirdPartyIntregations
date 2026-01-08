package com.example.emaildemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Recipient email is required")
    private String to;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Body is required")
    private String body;
    
    private String provider; // "ses" or "sendgrid"
    
    private List<EmailAttachment> attachments; // Support for multiple attachments
}
