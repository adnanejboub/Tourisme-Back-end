package com.tourisme.tourisme.service;

import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.entities.Touriste;
import com.tourisme.tourisme.repository.UtilisateurRepository;
import com.tourisme.tourisme.repository.TouristeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class UserProfileService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TouristeRepository touristeRepository;

    /**
     * Update user profile with optional fields
     * @param userId The user ID
     * @param profileData Map containing profile fields to update
     * @return Updated user profile
     */
    public Optional<Utilisateur> updateUserProfile(Long userId, java.util.Map<String, Object> profileData) {
        System.out.println("UserProfileService: Updating profile for userId: " + userId);
        System.out.println("UserProfileService: Profile data received: " + profileData);
        
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            System.out.println("UserProfileService: User not found for userId: " + userId);
            return Optional.empty();
        }

        Utilisateur user = userOpt.get();
        System.out.println("UserProfileService: Found user: " + user.getEmail());
        boolean updated = false;

        // Update Utilisateur fields
        if (profileData.containsKey("firstName") && profileData.get("firstName") != null) {
            System.out.println("UserProfileService: Updating firstName to: " + profileData.get("firstName"));
            user.setPrenom((String) profileData.get("firstName"));
            updated = true;
        }

        if (profileData.containsKey("lastName") && profileData.get("lastName") != null) {
            System.out.println("UserProfileService: Updating lastName to: " + profileData.get("lastName"));
            user.setNom((String) profileData.get("lastName"));
            updated = true;
        }

        if (profileData.containsKey("telephone") && profileData.get("telephone") != null) {
            System.out.println("UserProfileService: Updating telephone to: " + profileData.get("telephone"));
            user.setTelephone((String) profileData.get("telephone"));
            updated = true;
        }

        if (profileData.containsKey("adresse") && profileData.get("adresse") != null) {
            System.out.println("UserProfileService: Updating adresse to: " + profileData.get("adresse"));
            user.setAdresse((String) profileData.get("adresse"));
            updated = true;
        }

        if (profileData.containsKey("dateNaissance") && profileData.get("dateNaissance") != null) {
            try {
                String dateStr = (String) profileData.get("dateNaissance");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dateNaissance = sdf.parse(dateStr);
                user.setDateNaissance(dateNaissance);
                updated = true;
            } catch (ParseException e) {
                // Log error but continue with other updates
                System.err.println("Invalid date format for dateNaissance: " + profileData.get("dateNaissance"));
            }
        }

        if (profileData.containsKey("cin") && profileData.get("cin") != null) {
            user.setCin((String) profileData.get("cin"));
            updated = true;
        }

        // Update Touriste fields if they exist
        Optional<Touriste> touristeOpt = touristeRepository.findByUtilisateur(user);
        if (touristeOpt.isPresent()) {
            Touriste touriste = touristeOpt.get();
            
            if (profileData.containsKey("nationalite") && profileData.get("nationalite") != null) {
                System.out.println("UserProfileService: Updating nationalite to: " + profileData.get("nationalite"));
                touriste.setNationalite((String) profileData.get("nationalite"));
                updated = true;
            }

            if (profileData.containsKey("passeport") && profileData.get("passeport") != null) {
                System.out.println("UserProfileService: Updating passeport to: " + profileData.get("passeport"));
                touriste.setPasseport((String) profileData.get("passeport"));
                updated = true;
            }

            if (profileData.containsKey("preferences") && profileData.get("preferences") != null) {
                System.out.println("UserProfileService: Updating preferences to: " + profileData.get("preferences"));
                touriste.setPreferences((String) profileData.get("preferences"));
                updated = true;
            }

            if (profileData.containsKey("niveauLangue") && profileData.get("niveauLangue") != null) {
                System.out.println("UserProfileService: Updating niveauLangue to: " + profileData.get("niveauLangue"));
                touriste.setNiveauLangue((String) profileData.get("niveauLangue"));
                updated = true;
            }

            if (profileData.containsKey("budgetMax") && profileData.get("budgetMax") != null) {
                System.out.println("UserProfileService: Updating budgetMax to: " + profileData.get("budgetMax"));
                try {
                    if (profileData.get("budgetMax") instanceof Number) {
                        touriste.setBudgetMax(((Number) profileData.get("budgetMax")).floatValue());
                    } else if (profileData.get("budgetMax") instanceof String) {
                        touriste.setBudgetMax(Float.parseFloat((String) profileData.get("budgetMax")));
                    }
                    updated = true;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid budget format: " + profileData.get("budgetMax"));
                }
            }

            if (updated) {
                System.out.println("UserProfileService: Saving updated touriste");
                touristeRepository.save(touriste);
                System.out.println("UserProfileService: Touriste saved successfully");
            }
        }

        // Save user if any updates were made
        if (updated) {
            System.out.println("UserProfileService: Saving updated user");
            user = utilisateurRepository.save(user);
            System.out.println("UserProfileService: User saved successfully");
        } else {
            System.out.println("UserProfileService: No updates were made");
        }

        return Optional.of(user);
    }

    /**
     * Update user profile by email (for JWT-based authentication)
     * @param email The user email
     * @param profileData Map containing profile fields to update
     * @return Updated user profile
     */
    public Optional<Utilisateur> updateUserProfile(String email, java.util.Map<String, Object> profileData) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        return updateUserProfile(userOpt.get().getIdUtilisateur(), profileData);
    }

    /**
     * Get user profile completion status
     * @param userId The user ID
     * @return Profile completion information
     */
    public java.util.Map<String, Object> getProfileCompletionStatus(Long userId) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return java.util.Map.of("error", "User not found");
        }

        Utilisateur user = userOpt.get();
        Optional<Touriste> touristeOpt = touristeRepository.findByUtilisateur(user);

        java.util.Map<String, Object> completionStatus = new java.util.HashMap<>();
        java.util.List<String> completedFields = new java.util.ArrayList<>();
        java.util.List<String> missingFields = new java.util.ArrayList<>();

        // Check Utilisateur fields
        if (user.getTelephone() != null && !user.getTelephone().isBlank()) {
            completedFields.add("telephone");
        } else {
            missingFields.add("telephone");
        }

        if (user.getAdresse() != null && !user.getAdresse().isBlank()) {
            completedFields.add("adresse");
        } else {
            missingFields.add("adresse");
        }

        if (user.getDateNaissance() != null) {
            completedFields.add("dateNaissance");
        } else {
            missingFields.add("dateNaissance");
        }

        if (user.getCin() != null && !user.getCin().isBlank() && !user.getCin().equals("N/A")) {
            completedFields.add("cin");
        } else {
            missingFields.add("cin");
        }

        // Check Touriste fields
        if (touristeOpt.isPresent()) {
            Touriste touriste = touristeOpt.get();
            
            if (touriste.getNationalite() != null && !touriste.getNationalite().isBlank()) {
                completedFields.add("nationalite");
            } else {
                missingFields.add("nationalite");
            }

            if (touriste.getPasseport() != null && !touriste.getPasseport().isBlank()) {
                completedFields.add("passeport");
            } else {
                missingFields.add("passeport");
            }

            if (touriste.getPreferences() != null && !touriste.getPreferences().isBlank()) {
                completedFields.add("preferences");
            } else {
                missingFields.add("preferences");
            }

            if (touriste.getNiveauLangue() != null && !touriste.getNiveauLangue().isBlank()) {
                completedFields.add("niveauLangue");
            } else {
                missingFields.add("niveauLangue");
            }

            if (touriste.getBudgetMax() != null) {
                completedFields.add("budgetMax");
            } else {
                missingFields.add("budgetMax");
            }
        }

        int totalFields = completedFields.size() + missingFields.size();
        double completionPercentage = totalFields > 0 ? (double) completedFields.size() / totalFields * 100 : 0;

        completionStatus.put("completedFields", completedFields);
        completionStatus.put("missingFields", missingFields);
        completionStatus.put("completionPercentage", Math.round(completionPercentage * 100.0) / 100.0);
        completionStatus.put("isComplete", missingFields.isEmpty());
        completionStatus.put("totalFields", totalFields);

        return completionStatus;
    }

    /**
     * Get profile completion status for a user
     * @param email The user email
     * @return Profile completion information
     */
    public java.util.Map<String, Object> getProfileCompletion(String email) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return Map.of(
                "error", "user_not_found",
                "message", "User not found"
            );
        }

        Utilisateur user = userOpt.get();
        Optional<Touriste> touristeOpt = touristeRepository.findByUtilisateur(user);

        // Define required fields
        java.util.List<String> requiredFields = new ArrayList<>();
        requiredFields.add("email");
        requiredFields.add("firstName");
        requiredFields.add("lastName");
        requiredFields.add("telephone");
        requiredFields.add("adresse");
        requiredFields.add("dateNaissance");
        requiredFields.add("nationalite");
        requiredFields.add("passeport");

        // Check completed fields
        java.util.List<String> completedFields = new ArrayList<>();
        java.util.List<String> missingFields = new ArrayList<>();

        // Basic user fields
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            completedFields.add("email");
        } else {
            missingFields.add("email");
        }

        if (user.getPrenom() != null && !user.getPrenom().isBlank()) {
            completedFields.add("firstName");
        } else {
            missingFields.add("firstName");
        }

        if (user.getNom() != null && !user.getNom().isBlank()) {
            completedFields.add("lastName");
        } else {
            missingFields.add("lastName");
        }

        if (user.getTelephone() != null && !user.getTelephone().isBlank()) {
            completedFields.add("telephone");
        } else {
            missingFields.add("telephone");
        }

        if (user.getAdresse() != null && !user.getAdresse().isBlank()) {
            completedFields.add("adresse");
        } else {
            missingFields.add("adresse");
        }

        if (user.getDateNaissance() != null) {
            completedFields.add("dateNaissance");
        } else {
            missingFields.add("dateNaissance");
        }

        // Tourist-specific fields
        if (touristeOpt.isPresent()) {
            Touriste touriste = touristeOpt.get();
            
            if (touriste.getNationalite() != null && !touriste.getNationalite().isBlank()) {
                completedFields.add("nationalite");
            } else {
                missingFields.add("nationalite");
            }

            if (touriste.getPasseport() != null && !touriste.getPasseport().isBlank()) {
                completedFields.add("passeport");
            } else {
                missingFields.add("passeport");
            }
        } else {
            missingFields.add("nationalite");
            missingFields.add("passeport");
        }

        // Calculate completion percentage
        double completionPercentage = (double) completedFields.size() / requiredFields.size() * 100;
        boolean isComplete = missingFields.isEmpty();

        return Map.of(
            "completedFields", completedFields,
            "missingFields", missingFields,
            "completionPercentage", Math.round(completionPercentage * 10.0) / 10.0,
            "isComplete", isComplete,
            "totalFields", requiredFields.size(),
            "completedCount", completedFields.size(),
            "missingCount", missingFields.size()
        );
    }

    /**
     * Get complete user profile with both Utilisateur and Touriste data
     * @param email The user email
     * @return Complete profile information
     */
    public Map<String, Object> getCompleteProfile(String email) {
        System.out.println("UserProfileService: Getting complete profile for email: " + email);
        
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            System.out.println("UserProfileService: User not found for email: " + email);
            return Map.of(
                "error", "user_not_found",
                "message", "User not found"
            );
        }

        Utilisateur user = userOpt.get();
        System.out.println("UserProfileService: Found user: " + user.getEmail());
        System.out.println("UserProfileService: User firstName: " + user.getPrenom());
        System.out.println("UserProfileService: User lastName: " + user.getNom());
        
        Optional<Touriste> touristeOpt = touristeRepository.findByUtilisateur(user);

        Map<String, Object> profileData = new HashMap<>();
        
        // Basic user information
        profileData.put("id", user.getIdUtilisateur());
        profileData.put("email", user.getEmail());
        profileData.put("firstName", user.getPrenom());
        profileData.put("lastName", user.getNom());
        profileData.put("telephone", user.getTelephone());
        profileData.put("adresse", user.getAdresse());
        profileData.put("dateNaissance", user.getDateNaissance());
        profileData.put("cin", user.getCin());
        profileData.put("dateInscription", user.getDateInscription());
        
        System.out.println("UserProfileService: Profile data - firstName: " + user.getPrenom());
        System.out.println("UserProfileService: Profile data - lastName: " + user.getNom());
        System.out.println("UserProfileService: Profile data - telephone: " + user.getTelephone());
        System.out.println("UserProfileService: Profile data - adresse: " + user.getAdresse());
        
        // Role information
        if (user.getRole() != null) {
            profileData.put("role", user.getRole().getNomRole());
        }
        
        // Tourist-specific information
        if (touristeOpt.isPresent()) {
            Touriste touriste = touristeOpt.get();
            profileData.put("touristeId", touriste.getIdTouriste());
            profileData.put("nationalite", touriste.getNationalite());
            profileData.put("passeport", touriste.getPasseport());
            profileData.put("dateEntree", touriste.getDateEntree());
            profileData.put("dateSortie", touriste.getDateSortie());
            profileData.put("preferences", touriste.getPreferences());
            profileData.put("niveauLangue", touriste.getNiveauLangue());
            profileData.put("budgetMax", touriste.getBudgetMax());
            
            System.out.println("UserProfileService: Tourist data - nationalite: " + touriste.getNationalite());
            System.out.println("UserProfileService: Tourist data - passeport: " + touriste.getPasseport());
            System.out.println("UserProfileService: Tourist data - budgetMax: " + touriste.getBudgetMax());
        } else {
            System.out.println("UserProfileService: No tourist data found for user");
        }
        
        System.out.println("UserProfileService: Returning complete profile data: " + profileData);
        return profileData;
    }
}
