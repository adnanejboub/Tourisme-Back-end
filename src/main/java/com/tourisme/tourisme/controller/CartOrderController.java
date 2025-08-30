package com.tourisme.tourisme.controller;

import com.tourisme.tourisme.entities.Produit;
import com.tourisme.tourisme.repository.ProduitRepository;
import com.tourisme.tourisme.service.UserMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class CartOrderController {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private UserMappingService userMappingService;

    // In-memory carts per user (for demo). Replace with persistent storage.
    private final Map<Long, List<Map<String, Object>>> carts = new HashMap<>();

    private Long currentUserId() {
        return userMappingService.getCurrentUserId();
    }

    @GetMapping("/users/me/cart")
    public ResponseEntity<?> getCart() {
        List<Map<String, Object>> items = carts.getOrDefault(currentUserId(), new ArrayList<>());
        return ResponseEntity.ok(Map.of("items", items));
    }

    @PostMapping("/users/me/cart/items")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> payload) {
        Long productId = Long.valueOf(String.valueOf(payload.get("productId")));
        Integer quantity = Integer.valueOf(String.valueOf(payload.getOrDefault("quantity", 1)));
        String color = String.valueOf(payload.getOrDefault("color", ""));
        String size = String.valueOf(payload.getOrDefault("size", ""));

        Optional<Produit> productOpt = produitRepository.findById(productId);
        if (productOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Product not found"));

        Map<String, Object> item = new HashMap<>();
        item.put("id", UUID.randomUUID().toString());
        item.put("productId", productId);
        item.put("quantity", quantity);
        item.put("color", color);
        item.put("size", size);
        item.put("productName", productOpt.get().getNom());
        item.put("price", productOpt.get().getPrix());

        carts.computeIfAbsent(currentUserId(), k -> new ArrayList<>()).add(item);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/users/me/cart/items/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable String itemId, @RequestBody Map<String, Object> payload) {
        List<Map<String, Object>> items = carts.getOrDefault(currentUserId(), new ArrayList<>());
        for (Map<String, Object> item : items) {
            if (Objects.equals(item.get("id"), itemId)) {
                if (payload.containsKey("quantity")) {
                    item.put("quantity", Integer.valueOf(String.valueOf(payload.get("quantity"))));
                }
                return ResponseEntity.ok(item);
            }
        }
        return ResponseEntity.status(404).body(Map.of("error", "Item not found"));
    }

    @DeleteMapping("/users/me/cart/items/{itemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable String itemId) {
        List<Map<String, Object>> items = carts.getOrDefault(currentUserId(), new ArrayList<>());
        boolean removed = items.removeIf(i -> Objects.equals(i.get("id"), itemId));
        return removed ? ResponseEntity.ok(Map.of("message", "Removed")) : ResponseEntity.status(404).body(Map.of("error", "Item not found"));
    }

    @DeleteMapping("/users/me/cart")
    public ResponseEntity<?> clearCart() {
        carts.remove(currentUserId());
        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload) {
        List<Map<String, Object>> items = new ArrayList<>(carts.getOrDefault(currentUserId(), new ArrayList<>()));
        if (items.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));

        String orderId = String.valueOf(System.currentTimeMillis());
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("items", items);
        order.put("shippingAddress", payload.get("shippingAddress"));
        order.put("paymentMethod", payload.get("paymentMethod"));
        order.put("createdAt", new Date());

        // Clear cart after placing order
        carts.remove(currentUserId());

        return ResponseEntity.ok(order);
    }
}









