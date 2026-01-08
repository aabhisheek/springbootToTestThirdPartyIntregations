package com.example.emaildemo.controller;

import com.example.emaildemo.dto.ApiResponse;
import com.example.emaildemo.dto.PaymentRequest;
import com.example.emaildemo.dto.PaymentResponse;
import com.example.emaildemo.dto.PaymentVerificationRequest;
import com.example.emaildemo.service.RazorpayService;
import com.example.emaildemo.service.StripeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private final RazorpayService razorpayService;
    private final StripeService stripeService;
    
    // ==================== RAZORPAY ENDPOINTS ====================
    
    @PostMapping("/razorpay/create-order")
    public ResponseEntity<ApiResponse<PaymentResponse>> createRazorpayOrder(
            @Valid @RequestBody PaymentRequest request) {
        try {
            log.info("REST request to create Razorpay order");
            PaymentResponse response = razorpayService.createOrder(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Razorpay order created successfully"));
        } catch (Exception e) {
            log.error("Error creating Razorpay order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create Razorpay order: " + e.getMessage()));
        }
    }
    
    @PostMapping("/razorpay/verify")
    public ResponseEntity<ApiResponse<String>> verifyRazorpayPayment(
            @Valid @RequestBody PaymentVerificationRequest request) {
        try {
            log.info("REST request to verify Razorpay payment: {}", request.getPaymentId());
            
            boolean isValid = razorpayService.verifyPayment(
                    request.getOrderId(),
                    request.getPaymentId(),
                    request.getSignature()
            );
            
            if (isValid) {
                return ResponseEntity.ok(ApiResponse.success("verified", "Payment verified successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Payment verification failed"));
            }
        } catch (Exception e) {
            log.error("Error verifying payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to verify payment: " + e.getMessage()));
        }
    }
    
    @GetMapping("/razorpay/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getRazorpayPaymentDetails(
            @PathVariable String paymentId) {
        try {
            log.info("REST request to get Razorpay payment details: {}", paymentId);
            PaymentResponse response = razorpayService.getPaymentDetails(paymentId);
            return ResponseEntity.ok(ApiResponse.success(response, "Payment details fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching payment details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to fetch payment details: " + e.getMessage()));
        }
    }
    
    // ==================== STRIPE ENDPOINTS ====================
    
    @PostMapping("/stripe/create-payment-intent")
    public ResponseEntity<ApiResponse<PaymentResponse>> createStripePaymentIntent(
            @Valid @RequestBody PaymentRequest request) {
        try {
            log.info("REST request to create Stripe payment intent");
            PaymentResponse response = stripeService.createPaymentIntent(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Stripe payment intent created successfully"));
        } catch (Exception e) {
            log.error("Error creating Stripe payment intent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create Stripe payment intent: " + e.getMessage()));
        }
    }
    
    @GetMapping("/stripe/{paymentIntentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getStripePaymentDetails(
            @PathVariable String paymentIntentId) {
        try {
            log.info("REST request to get Stripe payment intent details: {}", paymentIntentId);
            PaymentResponse response = stripeService.getPaymentIntentDetails(paymentIntentId);
            return ResponseEntity.ok(ApiResponse.success(response, "Payment intent details fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching payment intent details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to fetch payment intent details: " + e.getMessage()));
        }
    }
    
    @PostMapping("/stripe/{paymentIntentId}/confirm")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmStripePayment(
            @PathVariable String paymentIntentId) {
        try {
            log.info("REST request to confirm Stripe payment: {}", paymentIntentId);
            PaymentResponse response = stripeService.confirmPayment(paymentIntentId);
            return ResponseEntity.ok(ApiResponse.success(response, "Payment confirmed successfully"));
        } catch (Exception e) {
            log.error("Error confirming payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to confirm payment: " + e.getMessage()));
        }
    }
    
    // ==================== GENERAL ENDPOINTS ====================
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("OK", "Payment service is running"));
    }
}
