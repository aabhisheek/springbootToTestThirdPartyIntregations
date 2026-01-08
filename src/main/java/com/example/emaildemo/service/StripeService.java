package com.example.emaildemo.service;

import com.example.emaildemo.dto.PaymentRequest;
import com.example.emaildemo.dto.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Service
@Slf4j
public class StripeService {
    
    @Value("${stripe.api.key}")
    private String apiKey;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }
    
    public PaymentResponse createPaymentIntent(PaymentRequest request) throws Exception {
        log.info("Creating Stripe payment intent for amount: {} {}", request.getAmount(), request.getCurrency());
        
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(request.getAmount()) // Amount in cents
                    .setCurrency(request.getCurrency().toLowerCase())
                    .setDescription(request.getDescription())
                    .putMetadata("customer_email", request.getCustomerEmail())
                    .putMetadata("customer_name", request.getCustomerName() != null ? request.getCustomerName() : "")
                    .putMetadata("order_id", request.getOrderId() != null ? request.getOrderId() : "")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();
            
            PaymentIntent intent = PaymentIntent.create(params);
            
            log.info("Stripe payment intent created successfully: {}", intent.getId());
            
            return PaymentResponse.builder()
                    .paymentId(intent.getId())
                    .orderId(intent.getId())
                    .amount(intent.getAmount())
                    .currency(intent.getCurrency().toUpperCase())
                    .status(intent.getStatus())
                    .provider("stripe")
                    .additionalData(Map.of(
                            "client_secret", intent.getClientSecret(),
                            "payment_intent_id", intent.getId(),
                            "customer_email", request.getCustomerEmail()
                    ))
                    .build();
            
        } catch (StripeException e) {
            log.error("Error creating Stripe payment intent: {}", e.getMessage());
            throw new Exception("Failed to create Stripe payment intent: " + e.getMessage());
        }
    }
    
    public PaymentResponse getPaymentIntentDetails(String paymentIntentId) throws Exception {
        log.info("Fetching Stripe payment intent details for: {}", paymentIntentId);
        
        try {
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
            
            return PaymentResponse.builder()
                    .paymentId(intent.getId())
                    .orderId(intent.getId())
                    .amount(intent.getAmount())
                    .currency(intent.getCurrency().toUpperCase())
                    .status(intent.getStatus())
                    .provider("stripe")
                    .additionalData(intent.toJson())
                    .build();
            
        } catch (StripeException e) {
            log.error("Error fetching payment intent details: {}", e.getMessage());
            throw new Exception("Failed to fetch payment intent details: " + e.getMessage());
        }
    }
    
    public PaymentResponse confirmPayment(String paymentIntentId) throws Exception {
        log.info("Confirming Stripe payment: {}", paymentIntentId);
        
        try {
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntent confirmedIntent = intent.confirm();
            
            return PaymentResponse.builder()
                    .paymentId(confirmedIntent.getId())
                    .orderId(confirmedIntent.getId())
                    .amount(confirmedIntent.getAmount())
                    .currency(confirmedIntent.getCurrency().toUpperCase())
                    .status(confirmedIntent.getStatus())
                    .provider("stripe")
                    .build();
            
        } catch (StripeException e) {
            log.error("Error confirming payment: {}", e.getMessage());
            throw new Exception("Failed to confirm payment: " + e.getMessage());
        }
    }
}
