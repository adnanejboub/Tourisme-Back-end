package com.tourisme.tourisme.service;

import com.tourisme.tourisme.dto.*;
import com.tourisme.tourisme.entities.*;
import com.tourisme.tourisme.repository.CartItemRepository;
import com.tourisme.tourisme.repository.OrderRepository;
import com.tourisme.tourisme.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UtilisateurRepository userRepository;

    @Transactional
    public String createOrder(Long userId, CreateOrderDto createOrderDto) {
        Utilisateur user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser_IdUtilisateurOrderByCreatedDateDesc(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUtilisateur(user);
        order.setShippingAddress(createOrderDto.getShippingAddress());
        order.setPaymentMethod(createOrderDto.getPaymentMethod());
        order.setNotes(createOrderDto.getNotes());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);

        // Calculate total amount and create order items
        double totalAmount = 0.0;
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSelectedColor(cartItem.getSelectedColor());
            orderItem.setSelectedSize(cartItem.getSelectedSize());

            // Use discounted price if available, otherwise regular price
            double itemPrice = (cartItem.getProduct().getDiscountedPrice() > 0)
                    ? cartItem.getProduct().getDiscountedPrice()
                    : cartItem.getProduct().getPrice();

            orderItem.setPrice(itemPrice);
            totalAmount += itemPrice * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cartItemRepository.deleteByUser_IdUtilisateur(userId);

        return savedOrder.getOrderNumber();
    }

    public Page<OrderDto> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUser_IdUtilisateurOrderByCreatedDateDesc(userId, pageable)
                .map(this::convertToDto);
    }

    public OrderDto getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return convertToDto(order);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ORD-" + timestamp + "-" + (int)(Math.random() * 1000);
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setTotalAmount(order.getTotalAmount()); // double
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setNotes(order.getNotes());
        dto.setCreatedDate(order.getCreatedDate());

        if (order.getOrderItems() != null) {
            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                    .map(this::convertOrderItemToDto)
                    .collect(Collectors.toList());
            dto.setOrderItems(orderItemDtos);
        }

        return dto;
    }

    private OrderItemDto convertOrderItemToDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice()); // double
        dto.setSelectedColor(orderItem.getSelectedColor());
        dto.setSelectedSize(orderItem.getSelectedSize());

        // Convert product
        Product product = orderItem.getProduct();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setDiscountedPrice(product.getDiscountedPrice());
        productDto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setTitle(product.getCategory().getTitle());
            productDto.setCategory(categoryDto);
        }

        dto.setProduct(productDto);
        return dto;
    }
}
