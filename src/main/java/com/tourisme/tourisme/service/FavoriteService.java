package com.tourisme.tourisme.service;

import com.tourisme.tourisme.entities.Favorite;
import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Map<String, Object> toggleFavorite(Utilisateur user, String type, Long itemId) {
        var existing = favoriteRepository.findByUtilisateurAndItemTypeAndItemId(user, type, itemId);
        Map<String, Object> result = new HashMap<>();
        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());
            result.put("action", "removed");
        } else {
            Favorite favorite = new Favorite(user, type, itemId);
            favoriteRepository.save(favorite);
            result.put("action", "added");
        }
        result.put("type", type);
        result.put("itemId", itemId);
        return result;
    }

    public List<Map<String, Object>> listFavorites(Utilisateur user) {
        return favoriteRepository.findByUtilisateurOrderByCreatedAtDesc(user)
                .stream()
                .map(f -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", f.getId());
                    m.put("type", f.getItemType());
                    m.put("itemId", f.getItemId());
                    m.put("createdAt", f.getCreatedAt());
                    return m;
                })
                .collect(Collectors.toList());
    }
}



