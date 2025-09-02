package com.tourisme.tourisme.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Integer quantity;
    private double price;
    private String selectedColor;
    private String selectedSize;
    private ProductDto product;
}
