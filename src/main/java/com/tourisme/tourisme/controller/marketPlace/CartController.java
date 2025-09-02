package com.tourisme.tourisme.controller.marketPlace;

import com.tourisme.tourisme.dto.CartItemDto;
import com.tourisme.tourisme.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        // For now, using a mock user ID. In real implementation, extract from JWT token
        Long userId = 1L; // This should be extracted from authenticated user
        List<CartItemDto> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // For now, using a mock user ID
        Long userId = 1L;
        Long productId = Long.valueOf(request.get("productId").toString());
        int quantity = (Integer) request.get("quantity");
        String selectedColor = (String) request.get("selectedColor");
        String selectedSize = (String) request.get("selectedSize");

        cartService.addToCart(userId, productId, quantity, selectedColor, selectedSize);
        return ResponseEntity.ok().body(Map.of("message", "Item added to cart successfully"));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long itemId) {
        cartService.removeFromCart(itemId);
        return ResponseEntity.ok().body(Map.of("message", "Item removed from cart"));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> request) {
        int quantity = (Integer) request.get("quantity");
        cartService.updateCartItemQuantity(itemId, quantity);
        return ResponseEntity.ok().body(Map.of("message", "Cart item updated"));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = 1L; // Mock user ID
        cartService.clearCart(userId);
        return ResponseEntity.ok().body(Map.of("message", "Cart cleared"));
    }
}
