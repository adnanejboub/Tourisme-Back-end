package com.tourisme.tourisme.service;

import com.tourisme.tourisme.dto.CartItemDto;
import com.tourisme.tourisme.dto.CategoryDto;
import com.tourisme.tourisme.dto.ProductDto;
import com.tourisme.tourisme.entities.CartItem;
import com.tourisme.tourisme.entities.Product;
import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.repository.CartItemRepository;
import com.tourisme.tourisme.repository.ProductRepository;
import com.tourisme.tourisme.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UtilisateurRepository userRepository;

    public List<CartItemDto> getCartItems(Long userId) {
        return cartItemRepository.findByUser_IdUtilisateurOrderByCreatedDateDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addToCart(Long userId, Long productId, int quantity, String selectedColor, String selectedSize) {
        Utilisateur user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository
                .findByUser_IdUtilisateurAndProduct_IdAndSelectedColorAndSelectedSize(userId, productId, selectedColor, selectedSize);

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Add new item
            CartItem cartItem = new CartItem();
            cartItem.setUtilisateur(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSelectedColor(selectedColor);
            cartItem.setSelectedSize(selectedSize);
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void removeFromCart(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void updateCartItemQuantity(Long itemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUser_IdUtilisateur(userId);
    }

    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setSelectedColor(cartItem.getSelectedColor());
        dto.setSelectedSize(cartItem.getSelectedSize());
        dto.setCreatedDate(cartItem.getCreatedDate());

        // Convert product
        Product product = cartItem.getProduct();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setDiscountedPrice(product.getDiscountedPrice());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setColors(product.getColors());
        productDto.setSizes(product.getSizes());

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
