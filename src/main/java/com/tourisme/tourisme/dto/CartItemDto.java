package com.tourisme.tourisme.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private String selectedColor;
    private String selectedSize;
    private LocalDateTime createdDate;
    private ProductDto product;
}