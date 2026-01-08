package com.example.emaildemo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationRequest {
    
    @NotBlank(message = "Payment ID is required")
    private String paymentId;
    
    private String orderId;
    
    private String signature; // For Razorpay signature verification
}
