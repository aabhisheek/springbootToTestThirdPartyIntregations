package com.example.emaildemo.service;

import com.example.emaildemo.dto.OrderDTO;
import com.example.emaildemo.entity.Order;
import com.example.emaildemo.entity.Product;
import com.example.emaildemo.entity.User;
import com.example.emaildemo.repository.OrderRepository;
import com.example.emaildemo.repository.ProductRepository;
import com.example.emaildemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info("Creating order for user ID: {}", orderDTO.getUserId());
        
        // Validate user exists
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + orderDTO.getUserId()));
        
        // Validate product exists
        Product product = productRepository.findById(orderDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + orderDTO.getProductId()));
        
        // Check stock availability
        if (product.getStockQuantity() < orderDTO.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        
        Order order = convertToEntity(orderDTO);
        
        // Calculate total amount
        BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(orderDTO.getQuantity()));
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.OrderStatus.PENDING);
        
        // Update product stock
        product.setStockQuantity(product.getStockQuantity() - orderDTO.getQuantity());
        productRepository.save(product);
        
        Order savedOrder = orderRepository.save(order);
        
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        return convertToDTO(savedOrder);
    }
    
    public List<OrderDTO> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        return convertToDTO(order);
    }
    
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user ID: {}", userId);
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO updateOrderStatus(Long id, Order.OrderStatus status) {
        log.info("Updating order status for ID: {} to {}", id, status);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Order status updated successfully");
        return convertToDTO(updatedOrder);
    }
    
    public void cancelOrder(Long id) {
        log.info("Cancelling order with ID: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel a delivered order");
        }
        
        // Restore product stock
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStockQuantity(product.getStockQuantity() + order.getQuantity());
        productRepository.save(product);
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
        
        log.info("Order cancelled successfully with ID: {}", id);
    }
    
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setProductId(order.getProductId());
        dto.setQuantity(order.getQuantity());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setNotes(order.getNotes());
        return dto;
    }
    
    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setProductId(dto.getProductId());
        order.setQuantity(dto.getQuantity());
        order.setNotes(dto.getNotes());
        return order;
    }
}
