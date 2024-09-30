package com.example.productcatalog.controller;

import com.example.productcatalog.model.*;
import com.example.productcatalog.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody User user) {
        return ResponseEntity.ok(orderService.placeOrder(user.getUserId(), user));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatus newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok().build();
    }

    // Shopping cart endpoints
    @PostMapping("/cart/add")
    public ResponseEntity<Void> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        orderService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getCart(userId));
    }

    @DeleteMapping("/cart/{userId}/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        orderService.removeFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        orderService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}