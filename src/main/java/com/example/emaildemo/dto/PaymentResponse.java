package com.example.emaildemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    
    private String paymentId;
    private String orderId;
    private String status;
    private Long amount;
    private String currency;
    private String provider; // "razorpay" or "stripe"
    private String paymentUrl; // For redirect-based payments
    private Object additionalData; // Provider-specific data
}
