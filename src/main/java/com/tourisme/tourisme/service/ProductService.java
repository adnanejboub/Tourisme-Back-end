package com.tourisme.tourisme.service;

import com.tourisme.tourisme.dto.CategoryDto;
import com.tourisme.tourisme.dto.ProductDto;
import com.tourisme.tourisme.entities.Product;
import com.tourisme.tourisme.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrueOrderByCreatedDateDesc(pageable)
                .map(this::convertToDto);
    }

    public Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndIsActiveTrueOrderByCreatedDateDesc(categoryId, pageable)
                .map(this::convertToDto);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDto(product);
    }

    public List<ProductDto> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedDateDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ProductDto> searchProducts(String query, Pageable pageable) {
        return productRepository.searchProducts(query, pageable)
                .map(this::convertToDto);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setColors(product.getColors());
        dto.setSizes(product.getSizes());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setRating(product.getRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setCreatedDate(product.getCreatedDate());
        dto.setFavorite(false); // This would be set based on user preferences

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setTitle(product.getCategory().getTitle());
            dto.setCategory(categoryDto);
        }

        return dto;
    }
}
