package com.example.emaildemo.controller;

import com.example.emaildemo.dto.ApiResponse;
import com.example.emaildemo.dto.OrderDTO;
import com.example.emaildemo.entity.Order;
import com.example.emaildemo.service.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderServiceImpl orderService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try {
            log.info("REST request to create order");
            OrderDTO createdOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdOrder, "Order created successfully"));
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
        try {
            log.info("REST request to get all orders");
            List<OrderDTO> orders = orderService.getAllOrders();
            return ResponseEntity.ok(ApiResponse.success(orders, "Orders fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching orders: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long id) {
        try {
            log.info("REST request to get order by ID: {}", id);
            OrderDTO order = orderService.getOrderById(id);
            return ResponseEntity.ok(ApiResponse.success(order, "Order fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUserId(@PathVariable Long userId) {
        try {
            log.info("REST request to get orders for user ID: {}", userId);
            List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(orders, "Orders fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching orders for user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        try {
            log.info("REST request to update order status for ID: {}", id);
            OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success(updatedOrder, "Order status updated successfully"));
        } catch (Exception e) {
            log.error("Error updating order status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long id) {
        try {
            log.info("REST request to cancel order with ID: {}", id);
            orderService.cancelOrder(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Order cancelled successfully"));
        } catch (Exception e) {
            log.error("Error cancelling order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
