package com.tourisme.tourisme.controller;

import com.tourisme.tourisme.entities.Sejour;
import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.repository.SejourRepository;
import com.tourisme.tourisme.service.UserMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class ItineraryTripController {

    @Autowired
    private SejourRepository sejourRepository;

    @Autowired
    private UserMappingService userMappingService;

    @PostMapping("/itineraries/generate")
    public ResponseEntity<?> generateItinerary(@RequestBody Map<String, Object> payload) {
        try {
            String destination = (String) payload.get("destination");
            Integer days = (Integer) payload.get("days");
            String budget = (String) payload.get("budget");
            @SuppressWarnings("unchecked")
            List<String> activities = (List<String>) payload.get("activities");
            
            // Validate input
            if (destination == null || destination.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "Destination is required"
                ));
            }
            
            if (days == null || days < 1 || days > 30) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error", 
                    "message", "Days must be between 1 and 30"
                ));
            }

            // Generate itinerary logic
            Map<String, Object> itinerary = new HashMap<>();
            itinerary.put("destination", destination);
            itinerary.put("days", days);
            itinerary.put("budget", budget);
            itinerary.put("activities", activities);
            itinerary.put("generatedAt", new Date());
            
            // Generate daily schedule
            List<Map<String, Object>> dailySchedule = new ArrayList<>();
            for (int day = 1; day <= days; day++) {
                Map<String, Object> daySchedule = new HashMap<>();
                daySchedule.put("day", day);
                daySchedule.put("date", new Date()); // TODO: Calculate actual date
                daySchedule.put("activities", generateDailyActivities(activities, budget));
                daySchedule.put("meals", generateMeals(budget));
                daySchedule.put("accommodation", generateAccommodation(budget));
                dailySchedule.add(daySchedule);
            }
            itinerary.put("dailySchedule", dailySchedule);
            
            // Calculate estimated costs
            Map<String, Object> costs = calculateEstimatedCosts(days, budget, activities);
            itinerary.put("estimatedCosts", costs);
            
            // Add recommendations
            itinerary.put("recommendations", generateRecommendations(destination, activities));
            
            return ResponseEntity.ok(itinerary);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "itinerary_generation_failed",
                "message", e.getMessage()
            ));
        }
    }

    private List<Map<String, Object>> generateDailyActivities(List<String> activities, String budget) {
        List<Map<String, Object>> dailyActivities = new ArrayList<>();
        
        // Sample activities based on budget
        if (activities != null && !activities.isEmpty()) {
            for (String activity : activities) {
                Map<String, Object> activityItem = new HashMap<>();
                activityItem.put("name", activity);
                activityItem.put("duration", "2-3 hours");
                activityItem.put("cost", calculateActivityCost(activity, budget));
                activityItem.put("location", "City center");
                dailyActivities.add(activityItem);
            }
        }
        
        return dailyActivities;
    }

    private List<Map<String, Object>> generateMeals(String budget) {
        List<Map<String, Object>> meals = new ArrayList<>();
        
        String[] mealTypes = {"Breakfast", "Lunch", "Dinner"};
        for (String mealType : mealTypes) {
            Map<String, Object> meal = new HashMap<>();
            meal.put("type", mealType);
            meal.put("suggestion", getMealSuggestion(budget));
            meal.put("estimatedCost", calculateMealCost(budget));
            meals.add(meal);
        }
        
        return meals;
    }

    private Map<String, Object> generateAccommodation(String budget) {
        Map<String, Object> accommodation = new HashMap<>();
        accommodation.put("type", getAccommodationType(budget));
        accommodation.put("estimatedCost", calculateAccommodationCost(budget));
        accommodation.put("location", "City center");
        return accommodation;
    }

    private Map<String, Object> calculateEstimatedCosts(Integer days, String budget, List<String> activities) {
        Map<String, Object> costs = new HashMap<>();
        
        double accommodationCost = calculateAccommodationCost(budget) * days;
        double mealsCost = calculateMealCost(budget) * 3 * days; // 3 meals per day
        double activitiesCost = activities != null ? activities.size() * 50.0 : 0; // 50 per activity
        double transportCost = days * 20.0; // 20 per day for transport
        
        double totalCost = accommodationCost + mealsCost + activitiesCost + transportCost;
        
        costs.put("accommodation", accommodationCost);
        costs.put("meals", mealsCost);
        costs.put("activities", activitiesCost);
        costs.put("transport", transportCost);
        costs.put("total", totalCost);
        costs.put("currency", "MAD");
        
        return costs;
    }

    private List<String> generateRecommendations(String destination, List<String> activities) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("Visit the main attractions early in the morning to avoid crowds");
        recommendations.add("Try local cuisine at traditional restaurants");
        recommendations.add("Book activities in advance during peak season");
        recommendations.add("Carry cash for small purchases and tips");
        return recommendations;
    }

    private double calculateActivityCost(String activity, String budget) {
        switch (budget) {
            case "budget": return 30.0;
            case "mid_range": return 70.0;
            case "luxury": return 150.0;
            default: return 50.0;
        }
    }

    private String getMealSuggestion(String budget) {
        switch (budget) {
            case "budget": return "Local street food or simple restaurants";
            case "mid_range": return "Traditional restaurants with local cuisine";
            case "luxury": return "Fine dining restaurants";
            default: return "Local restaurants";
        }
    }

    private double calculateMealCost(String budget) {
        switch (budget) {
            case "budget": return 50.0;
            case "mid_range": return 120.0;
            case "luxury": return 300.0;
            default: return 80.0;
        }
    }

    private String getAccommodationType(String budget) {
        switch (budget) {
            case "budget": return "Hostel or budget hotel";
            case "mid_range": return "3-4 star hotel";
            case "luxury": return "5 star hotel or luxury riad";
            default: return "Hotel";
        }
    }

    private double calculateAccommodationCost(String budget) {
        switch (budget) {
            case "budget": return 200.0;
            case "mid_range": return 500.0;
            case "luxury": return 1200.0;
            default: return 350.0;
        }
    }

    @GetMapping("/users/me/trips")
    public ResponseEntity<List<Sejour>> listTrips() {
        // For demo, return all trips (ideally filter by current user via relation Touriste -> Utilisateur)
        return ResponseEntity.ok(sejourRepository.findAll());
    }

    @PostMapping("/users/me/trips")
    public ResponseEntity<Sejour> createTrip(@RequestBody Sejour sejour) {
        return ResponseEntity.ok(sejourRepository.save(sejour));
    }

    @GetMapping("/users/me/trips/{id}")
    public ResponseEntity<?> getTrip(@PathVariable Long id) {
        return sejourRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/me/trips/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable Long id, @RequestBody Sejour sejour) {
        if (!sejourRepository.existsById(id)) return ResponseEntity.notFound().build();
        sejour.setIdSejour(id);
        return ResponseEntity.ok(sejourRepository.save(sejour));
    }

    @DeleteMapping("/users/me/trips/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long id) {
        if (!sejourRepository.existsById(id)) return ResponseEntity.notFound().build();
        sejourRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Trip deleted"));
    }
}









