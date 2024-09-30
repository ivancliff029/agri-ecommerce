package com.example.productcatalog.service;

import com.example.productcatalog.model.Order;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendOrderConfirmation(String email, Order order) {
        // Implement email sending logic
        System.out.println("Sending order confirmation email to " + email + " for order " + order.getOrderId());
    }

    public void sendOrderCancellation(String email, Order order) {
        // Implement email sending logic
        System.out.println("Sending order cancellation email to " + email + " for order " + order.getOrderId());
    }

    public void sendOrderStatusUpdate(String email, Order order) {
        // Implement email sending logic
        System.out.println("Sending order status update email to " + email + " for order " + order.getOrderId());
    }
}
