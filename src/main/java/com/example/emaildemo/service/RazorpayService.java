package com.example.emaildemo.service;

import com.example.emaildemo.dto.PaymentRequest;
import com.example.emaildemo.dto.PaymentResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RazorpayService {
    
    @Value("${razorpay.key.id}")
    private String keyId;
    
    @Value("${razorpay.key.secret}")
    private String keySecret;
    
    private RazorpayClient getRazorpayClient() throws RazorpayException {
        return new RazorpayClient(keyId, keySecret);
    }
    
    public PaymentResponse createOrder(PaymentRequest request) throws Exception {
        log.info("Creating Razorpay order for amount: {} {}", request.getAmount(), request.getCurrency());
        
        try {
            RazorpayClient client = getRazorpayClient();
            
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount()); // Amount in paise
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", request.getOrderId() != null ? request.getOrderId() : "receipt_" + System.currentTimeMillis());
            
            // Add customer notes
            JSONObject notes = new JSONObject();
            notes.put("email", request.getCustomerEmail());
            if (request.getCustomerName() != null) {
                notes.put("name", request.getCustomerName());
            }
            if (request.getDescription() != null) {
                notes.put("description", request.getDescription());
            }
            orderRequest.put("notes", notes);
            
            Order order = client.orders.create(orderRequest);
            
            log.info("Razorpay order created successfully: {}", String.valueOf(order.get("id")));
            
            return PaymentResponse.builder()
                    .orderId(String.valueOf(order.get("id")))
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(String.valueOf(order.get("status")))
                    .provider("razorpay")
                    .additionalData(Map.of(
                            "key", keyId,
                            "order_id", String.valueOf(order.get("id")),
                            "customer_email", request.getCustomerEmail(),
                            "customer_name", request.getCustomerName() != null ? request.getCustomerName() : ""
                    ))
                    .build();
            
        } catch (RazorpayException e) {
            log.error("Error creating Razorpay order: {}", e.getMessage());
            throw new Exception("Failed to create Razorpay order: " + e.getMessage());
        }
    }
    
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        log.info("Verifying Razorpay payment: {}", paymentId);
        
        try {
            String payload = orderId + "|" + paymentId;
            
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
            mac.init(secretKey);
            
            byte[] hash = mac.doFinal(payload.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            String generatedSignature = hexString.toString();
            boolean isValid = generatedSignature.equals(signature);
            
            log.info("Payment verification result: {}", isValid);
            return isValid;
            
        } catch (Exception e) {
            log.error("Error verifying payment: {}", e.getMessage());
            return false;
        }
    }
    
    public PaymentResponse getPaymentDetails(String paymentId) throws Exception {
        log.info("Fetching Razorpay payment details for: {}", paymentId);
        
        try {
            RazorpayClient client = getRazorpayClient();
            com.razorpay.Payment payment = client.payments.fetch(paymentId);
            
            return PaymentResponse.builder()
                    .paymentId(String.valueOf(payment.get("id")))
                    .orderId(String.valueOf(payment.get("order_id")))
                    .amount(Long.valueOf(String.valueOf(payment.get("amount"))))
                    .currency(String.valueOf(payment.get("currency")))
                    .status(String.valueOf(payment.get("status")))
                    .provider("razorpay")
                    .additionalData(payment.toJson())
                    .build();
            
        } catch (RazorpayException e) {
            log.error("Error fetching payment details: {}", e.getMessage());
            throw new Exception("Failed to fetch payment details: " + e.getMessage());
        }
    }
}
