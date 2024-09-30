package com.example.productcatalog.service;

import com.example.productcatalog.model.*;
import com.example.productcatalog.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private EmailService emailService;

    private Map<Long, List<CartItem>> userCarts = new HashMap<>();

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUser_UserId(userId);
    }

    @Transactional
    public Order placeOrder(Long userId, User user) {
        List<CartItem> cartItems = userCarts.get(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderItems.add(orderItem);

            totalPrice += orderItem.getPrice();

            // Update product quantity
            productService.updateProductQuantity(cartItem.getProduct().getProductID(), -cartItem.getQuantity());
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        userCarts.remove(userId);

        // Send order confirmation email
        emailService.sendOrderConfirmation(user.getEmail(), savedOrder);

        return savedOrder;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PROCESSING) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            // Revert product quantities
            for (OrderItem item : order.getOrderItems()) {
                productService.updateProductQuantity(item.getProduct().getProductID(), item.getQuantity());
            }

            // Send cancellation email
            emailService.sendOrderCancellation(order.getUser().getEmail(), order);
        } else {
            throw new IllegalStateException("Order cannot be cancelled");
        }
    }

    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        order.setStatus(newStatus);
        orderRepository.save(order);

        // Send status update email
        emailService.sendOrderStatusUpdate(order.getUser().getEmail(), order);
    }

    // Shopping cart methods
    public void addToCart(Long userId, Long productId, int quantity) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        userCarts.computeIfAbsent(userId, k -> new ArrayList<>());
        List<CartItem> cart = userCarts.get(userId);

        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getProduct().getProductID().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            cart.add(new CartItem(product, quantity));
        }
    }

    public List<CartItem> getCart(Long userId) {
        return userCarts.getOrDefault(userId, new ArrayList<>());
    }

    public void removeFromCart(Long userId, Long productId) {
        List<CartItem> cart = userCarts.get(userId);
        if (cart != null) {
            cart.removeIf(item -> item.getProduct().getProductID().equals(productId));
        }
    }

    public void clearCart(Long userId) {
        userCarts.remove(userId);
    }
}