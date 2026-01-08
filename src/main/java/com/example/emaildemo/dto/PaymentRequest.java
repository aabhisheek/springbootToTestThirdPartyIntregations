package com.example.emaildemo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1")
    private Long amount; // Amount in smallest currency unit (paise for INR, cents for USD)
    
    @NotBlank(message = "Currency is required")
    private String currency; // INR, USD, etc.
    
    @NotBlank(message = "Customer email is required")
    private String customerEmail;
    
    private String customerName;
    
    private String customerPhone;
    
    private String description;
    
    private String orderId; // Optional: Your order reference
}
