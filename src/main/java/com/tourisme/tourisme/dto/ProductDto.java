package com.tourisme.tourisme.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private double discountedPrice;
    private String imageUrl;
    private List<String> colors;
    private List<String> sizes;
    private Integer stockQuantity;
    private Boolean isActive;
    private Boolean isFeatured;
    private double rating;
    private Integer reviewCount;
    private Date createdDate;
    private CategoryDto category;
    private boolean isFavorite;
}