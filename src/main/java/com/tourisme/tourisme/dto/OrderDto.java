package com.tourisme.tourisme.dto;

import com.tourisme.tourisme.entities.Order;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private double totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private Order.OrderStatus orderStatus;
    private Order.PaymentStatus paymentStatus;
    private String notes;
    private LocalDateTime createdDate;
    private List<OrderItemDto> orderItems;
}
