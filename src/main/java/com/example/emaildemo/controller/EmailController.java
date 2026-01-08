package com.example.emaildemo.controller;

import com.example.emaildemo.dto.ApiResponse;
import com.example.emaildemo.dto.EmailAttachment;
import com.example.emaildemo.dto.EmailRequest;
import com.example.emaildemo.dto.EmailRequestMultipart;
import com.example.emaildemo.service.EmailService;
import com.example.emaildemo.service.EmailServiceFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EmailController {
    
    private final EmailServiceFactory emailServiceFactory;
    
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            log.info("REST request to send email to: {}", emailRequest.getTo());
            
            EmailService emailService = emailServiceFactory.getEmailService(emailRequest.getProvider());
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            
            String message = String.format("Email sent successfully via %s", emailService.getProviderName());
            return ResponseEntity.ok(ApiResponse.success(emailService.getProviderName(), message));
            
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email: " + e.getMessage()));
        }
    }
    
    @PostMapping("/send-ses")
    public ResponseEntity<ApiResponse<String>> sendEmailViaSES(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            log.info("REST request to send email via AWS SES to: {}", emailRequest.getTo());
            
            EmailService emailService = emailServiceFactory.getEmailService("ses");
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            
            return ResponseEntity.ok(ApiResponse.success("ses", "Email sent successfully via AWS SES"));
            
        } catch (Exception e) {
            log.error("Error sending email via AWS SES: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email via AWS SES: " + e.getMessage()));
        }
    }
    
    @PostMapping("/send-sendgrid")
    public ResponseEntity<ApiResponse<String>> sendEmailViaSendGrid(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            log.info("REST request to send email via SendGrid to: {}", emailRequest.getTo());
            
            EmailService emailService = emailServiceFactory.getEmailService("sendgrid");
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            
            return ResponseEntity.ok(ApiResponse.success("sendgrid", "Email sent successfully via SendGrid"));
            
        } catch (Exception e) {
            log.error("Error sending email via SendGrid: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email via SendGrid: " + e.getMessage()));
        }
    }
    
    @PostMapping("/send-with-attachment")
    public ResponseEntity<ApiResponse<String>> sendEmailWithAttachment(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            log.info("REST request to send email with attachments to: {}", emailRequest.getTo());
            
            EmailService emailService = emailServiceFactory.getEmailService(emailRequest.getProvider());
            
            if (emailRequest.getAttachments() != null && !emailRequest.getAttachments().isEmpty()) {
                emailService.sendEmailWithAttachments(
                        emailRequest.getTo(), 
                        emailRequest.getSubject(), 
                        emailRequest.getBody(),
                        emailRequest.getAttachments()
                );
            } else {
                emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            }
            
            String message = String.format("Email with %d attachment(s) sent successfully via %s", 
                    emailRequest.getAttachments() != null ? emailRequest.getAttachments().size() : 0,
                    emailService.getProviderName());
            return ResponseEntity.ok(ApiResponse.success(emailService.getProviderName(), message));
            
        } catch (Exception e) {
            log.error("Error sending email with attachments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email with attachments: " + e.getMessage()));
        }
    }
    
    @PostMapping("/send-ses-with-attachment")
    public ResponseEntity<ApiResponse<String>> sendEmailViaSESWithAttachment(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            log.info("REST request to send email with attachments via AWS SES to: {}", emailRequest.getTo());
            
            EmailService emailService = emailServiceFactory.getEmailService("ses");
            
            if (emailRequest.getAttachments() != null && !emailRequest.getAttachments().isEmpty()) {
                emailService.sendEmailWithAttachments(
                        emailRequest.getTo(), 
                        emailRequest.getSubject(), 
                        emailRequest.getBody(),
                        emailRequest.getAttachments()
                );
            } else {
                emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            }
            
            String message = String.format("Email with %d attachment(s) sent successfully via AWS SES", 
                    emailRequest.getAttachments() != null ? emailRequest.getAttachments().size() : 0);
            return ResponseEntity.ok(ApiResponse.success("ses", message));
            
        } catch (Exception e) {
            log.error("Error sending email with attachments via AWS SES: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email with attachments via AWS SES: " + e.getMessage()));
        }
    }
    
    @PostMapping("/send-sendgrid-with-attachment")
    public ResponseEntity<ApiResponse<String>> sendEmailViaSendGridWithAttachment(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            log.info("REST request to send email with attachments via SendGrid to: {}", emailRequest.getTo());
            
            EmailService emailService = emailServiceFactory.getEmailService("sendgrid");
            
            if (emailRequest.getAttachments() != null && !emailRequest.getAttachments().isEmpty()) {
                emailService.sendEmailWithAttachments(
                        emailRequest.getTo(), 
                        emailRequest.getSubject(), 
                        emailRequest.getBody(),
                        emailRequest.getAttachments()
                );
            } else {
                emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            }
            
            String message = String.format("Email with %d attachment(s) sent successfully via SendGrid", 
                    emailRequest.getAttachments() != null ? emailRequest.getAttachments().size() : 0);
            return ResponseEntity.ok(ApiResponse.success("sendgrid", message));
            
        } catch (Exception e) {
            log.error("Error sending email with attachments via SendGrid: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email with attachments via SendGrid: " + e.getMessage()));
        }
    }
    
    @PostMapping(value = "/send-with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> sendEmailWithFile(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestParam(value = "provider", defaultValue = "ses") String provider,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            log.info("REST request to send email with {} file(s) to: {}", files.size(), to);
            
            // Convert MultipartFile to EmailAttachment
            List<EmailAttachment> attachments = new ArrayList<>();
            for (MultipartFile file : files) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setFileName(file.getOriginalFilename());
                attachment.setContentType(file.getContentType());
                attachment.setContent(file.getBytes());
                attachments.add(attachment);
            }
            
            EmailService emailService = emailServiceFactory.getEmailService(provider);
            emailService.sendEmailWithAttachments(to, subject, body, attachments);
            
            String message = String.format("Email with %d file(s) sent successfully via %s", 
                    files.size(), emailService.getProviderName());
            return ResponseEntity.ok(ApiResponse.success(emailService.getProviderName(), message));
            
        } catch (Exception e) {
            log.error("Error sending email with files: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email with files: " + e.getMessage()));
        }
    }
    
    @PostMapping(value = "/send-ses-with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> sendEmailViaSESWithFile(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            log.info("REST request to send email with {} file(s) via AWS SES to: {}", files.size(), to);
            
            // Convert MultipartFile to EmailAttachment
            List<EmailAttachment> attachments = new ArrayList<>();
            for (MultipartFile file : files) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setFileName(file.getOriginalFilename());
                attachment.setContentType(file.getContentType());
                attachment.setContent(file.getBytes());
                attachments.add(attachment);
            }
            
            EmailService emailService = emailServiceFactory.getEmailService("ses");
            emailService.sendEmailWithAttachments(to, subject, body, attachments);
            
            String message = String.format("Email with %d file(s) sent successfully via AWS SES", files.size());
            return ResponseEntity.ok(ApiResponse.success("ses", message));
            
        } catch (Exception e) {
            log.error("Error sending email with files via AWS SES: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email with files via AWS SES: " + e.getMessage()));
        }
    }
    
    @PostMapping(value = "/send-sendgrid-with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> sendEmailViaSendGridWithFile(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            log.info("REST request to send email with {} file(s) via SendGrid to: {}", files.size(), to);
            
            // Convert MultipartFile to EmailAttachment
            List<EmailAttachment> attachments = new ArrayList<>();
            for (MultipartFile file : files) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setFileName(file.getOriginalFilename());
                attachment.setContentType(file.getContentType());
                attachment.setContent(file.getBytes());
                attachments.add(attachment);
            }
            
            EmailService emailService = emailServiceFactory.getEmailService("sendgrid");
            emailService.sendEmailWithAttachments(to, subject, body, attachments);
            
            String message = String.format("Email with %d file(s) sent successfully via SendGrid", files.size());
            return ResponseEntity.ok(ApiResponse.success("sendgrid", message));
            
        } catch (Exception e) {
            log.error("Error sending email with files via SendGrid: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send email with files via SendGrid: " + e.getMessage()));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("OK", "Email service is running"));
    }
}
