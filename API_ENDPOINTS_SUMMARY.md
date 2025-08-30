# API Endpoints Summary - Tourisme Backend

## 🔐 Authentification (AuthController)

### Endpoints existants
- `POST /auth/register` - Inscription utilisateur
- `POST /auth/login` - Connexion utilisateur
- `POST /auth/logout` - Déconnexion
- `GET /auth/me` - Profil utilisateur actuel
- `POST /auth/token/refresh` - Rafraîchissement de token
- `POST /auth/password/forgot` - Mot de passe oublié
- `POST /auth/password/reset` - Réinitialisation mot de passe
- `POST /auth/email/resend-verification` - Renvoi vérification email

### Endpoints ajoutés
- `POST /auth/email/verify` - Vérification email
- `POST /auth/password/change` - Changement mot de passe

## 👤 Utilisateurs (UserController)

### Endpoints existants
- `GET /api/user/profile` - Profil utilisateur
- `PUT /api/user/me` - Mise à jour profil
- `PUT /api/user/me/preferences` - Mise à jour préférences

### Endpoints ajoutés
- `GET /api/user/favorites` - Favoris utilisateur
- `POST /api/user/favorites` - Ajouter favori
- `DELETE /api/user/favorites/{id}` - Supprimer favori
- `GET /api/user/orders` - Historique commandes

## 📋 Profil (UserProfileController)

### Endpoints existants
- `GET /api/profile/fields` - Champs disponibles profil

### Endpoints complétés
- `PUT /api/profile/update` - Mise à jour profil (implémenté)
- `GET /api/profile/completion` - Statut complétion profil (implémenté)

## 🏙️ Villes & Destinations (PublicController)

### Endpoints existants
- `GET /public/cities` - Liste des villes
- `GET /public/cities/{id}` - Détail ville
- `GET /public/activities` - Liste des activités
- `GET /public/activities/{id}` - Détail activité
- `GET /public/search` - Recherche globale
- `GET /public/products` - Catalogue produits
- `GET /public/products/{id}` - Détail produit

### Endpoints ajoutés
- `GET /public/cities/{id}/activities` - Activités par ville
- `GET /public/cities/{id}/attractions` - Attractions par ville
- `GET /public/cities/{id}/events` - Événements par ville
- `GET /public/cities/popular` - Villes populaires
- `GET /public/cities/recommended` - Villes recommandées
- `GET /public/activities/recommended` - Activités recommandées
- `GET /public/activities/by-category/{categoryId}` - Activités par catégorie
- `POST /public/activities/{id}/reviews` - Ajouter avis activité
- `GET /public/activities/{id}/reviews` - Avis d'une activité

## 📅 Événements (EventController) - NOUVEAU

### Endpoints ajoutés
- `GET /api/events` - Liste événements
- `GET /api/events/{id}` - Détail événement
- `GET /api/events/by-city/{cityId}` - Événements par ville
- `GET /api/events/upcoming` - Événements à venir
- `POST /api/events/{id}/book` - Réserver événement

## 🗺️ Voyages (TripController) - NOUVEAU

### Endpoints ajoutés
- `GET /api/trips` - Voyages sauvegardés
- `POST /api/trips` - Créer voyage sauvegardé
- `GET /api/trips/{id}` - Détail voyage sauvegardé
- `PUT /api/trips/{id}` - Modifier voyage sauvegardé
- `DELETE /api/trips/{id}` - Supprimer voyage sauvegardé
- `POST /api/trips/{id}/activities` - Ajouter activité au voyage
- `DELETE /api/trips/{id}/activities/{activityId}` - Supprimer activité du voyage

## 📱 Notifications (NotificationController) - NOUVEAU

### Endpoints ajoutés
- `GET /api/notifications` - Notifications utilisateur
- `POST /api/notifications` - Envoyer notification
- `PUT /api/notifications/{id}/read` - Marquer comme lu
- `DELETE /api/notifications/{id}` - Supprimer notification

## 🔍 Recherche (SearchController) - NOUVEAU

### Endpoints ajoutés
- `GET /api/search/advanced` - Recherche avancée
- `GET /api/search/filters` - Filtres de recherche
- `POST /api/search/suggestions` - Suggestions de recherche

## 🗺️ Itinéraires (ItineraryTripController)

### Endpoints existants
- `GET /api/users/me/trips` - Mes voyages
- `POST /api/users/me/trips` - Créer voyage
- `GET /api/users/me/trips/{id}` - Détail voyage
- `PUT /api/users/me/trips/{id}` - Modifier voyage
- `DELETE /api/users/me/trips/{id}` - Supprimer voyage

### Endpoints complétés
- `POST /api/itineraries/generate` - Générer itinéraire (logique avancée implémentée)

## 🛒 Panier & Commandes (CartOrderController)

### Endpoints existants
- `GET /api/users/me/cart` - Mon panier
- `POST /api/users/me/cart/items` - Ajouter au panier
- `PATCH /api/users/me/cart/items/{itemId}` - Modifier article panier
- `DELETE /api/users/me/cart/items/{itemId}` - Supprimer article panier
- `DELETE /api/users/me/cart` - Vider panier
- `POST /api/orders` - Créer commande

## 📋 Réservations (UserReservationsController)

### Endpoints existants
- `GET /api/users/me/reservations` - Mes réservations
- `POST /api/users/me/reservations` - Créer réservation
- `GET /api/users/me/reservations/{id}` - Détail réservation
- `PUT /api/users/me/reservations/{id}` - Modifier réservation
- `DELETE /api/users/me/reservations/{id}` - Supprimer réservation

## 👨‍💼 Admin (AdminController)

### Endpoints existants
- `GET /admin/users` - Liste utilisateurs
- `GET /admin/users/search` - Recherche utilisateurs
- `GET /admin/users/export` - Export CSV utilisateurs
- `GET /admin/users/{id}` - Détail utilisateur
- `PUT /admin/users/{id}/block` - Bloquer utilisateur
- `PUT /admin/users/{id}/unblock` - Débloquer utilisateur
- `DELETE /admin/users/{id}` - Supprimer utilisateur
- `GET /admin/cities` - Gestion villes
- `POST /admin/cities` - Créer ville
- `PUT /admin/cities/{id}` - Modifier ville
- `DELETE /admin/cities/{id}` - Supprimer ville
- `GET /admin/activities` - Gestion activités
- `POST /admin/activities` - Créer activité
- `PUT /admin/activities/{id}` - Modifier activité
- `DELETE /admin/activities/{id}` - Supprimer activité
- `GET /admin/products` - Gestion produits
- `POST /admin/products` - Créer produit
- `PUT /admin/products/{id}` - Modifier produit
- `DELETE /admin/products/{id}` - Supprimer produit
- `GET /admin/orders` - Gestion commandes
- `GET /admin/reservations` - Gestion réservations
- `GET /admin/statistics` - Statistiques

## 🧳 Touriste (TouristController)

### Endpoints existants
- `GET /tourist/villes` - Villes pour touristes
- `GET /tourist/villes/{id}` - Détail ville touriste
- `GET /tourist/villes/search` - Recherche villes
- `GET /tourist/villes/plage` - Villes plage
- `GET /tourist/villes/montagne` - Villes montagne
- `GET /tourist/villes/desert` - Villes désert
- `GET /tourist/villes/historique` - Villes historiques
- `GET /tourist/villes/culturelle` - Villes culturelles
- `GET /tourist/villes/moderne` - Villes modernes
- `GET /tourist/activites` - Activités pour touristes
- `GET /tourist/activites/{id}` - Détail activité touriste
- `GET /tourist/activites/search` - Recherche activités
- `GET /tourist/activites/ville/{villeId}` - Activités par ville
- `GET /tourist/activites/prix` - Activités par prix
- `GET /tourist/activites/duree` - Activités par durée
- `GET /tourist/activites/saison` - Activités par saison

## 🔧 Configuration

### CORS
- Configuration mise à jour pour supporter Flutter
- Origines autorisées : localhost, 127.0.0.1, 10.0.2.2 (émulateur Android)
- Méthodes autorisées : GET, POST, PUT, DELETE, OPTIONS, PATCH
- Headers autorisés : Origin, Content-Type, Accept, Authorization, etc.

### Sécurité
- Keycloak intégré pour l'authentification
- JWT tokens pour l'autorisation
- Rôles : USER, ADMIN, TOURISTE
- Endpoints publics : `/public/**`, `/auth/**`, `/api/events/**`, `/api/search/**`
- Endpoints authentifiés : `/api/**` (sauf events et search)
- Endpoints admin : `/admin/**`
- Endpoints touriste : `/tourist/**`

## 📊 Statistiques

- **Total endpoints implémentés** : ~85
- **Endpoints ajoutés** : ~25
- **Endpoints complétés** : ~4
- **Nouveaux contrôleurs** : 4 (EventController, TripController, NotificationController, SearchController)
- **Services mis à jour** : 3 (VilleService, ActiviteService, UserProfileService)

## 🚀 Prochaines étapes

1. Implémenter les services manquants (EventService, TripService, NotificationService, SearchService)
2. Ajouter la persistance pour les favoris, notifications, et voyages sauvegardés
3. Implémenter la logique de recommandation avancée
4. Ajouter la gestion des avis et commentaires
5. Implémenter les notifications push
6. Ajouter la recherche Elasticsearch
7. Implémenter la gestion des événements
8. Ajouter la gestion des attractions (monuments, etc.)
