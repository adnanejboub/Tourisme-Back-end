package com.tourisme.tourisme.controller.marketPlace;

import com.tourisme.tourisme.dto.CreateOrderDto;
import com.tourisme.tourisme.dto.OrderDto;
import com.tourisme.tourisme.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody CreateOrderDto createOrderDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Mock user ID for now
        Long userId = 1L;
        String orderNumber = orderService.createOrder(userId, createOrderDto);
        return ResponseEntity.ok().body(Map.of(
                "message", "Order created successfully",
                "orderNumber", orderNumber
        ));
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getUserOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = 1L; // Mock user ID
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByNumber(@PathVariable String orderNumber) {
        OrderDto order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
}