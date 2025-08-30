# Tourisme Backend API

A Spring Boot application for tourism management with Keycloak authentication and comprehensive database modeling.

## Features

- **Authentication**: Keycloak integration for secure user authentication
- **Database Modeling**: Complete UML-based entity relationships
- **Administrative Tasks**: Full admin panel with all required functionalities
- **Tourist Features**: Comprehensive tourist exploration and booking system
- **Content Management**: Add/modify activities, monuments, cities, etc.
- **Statistics**: Dynamic statistics and reporting
- **Transaction Management**: Payment history, refunds, commissions

## Database Entities

### Core Entities
- **Ville**: Cities with characteristics (beach, mountain, desert, etc.)
- **Activite**: Activities with types, prices, duration
- **Monument**: Historical and cultural monuments
- **Hebergement**: Accommodation (Hotel, Riad, Apartment)
- **Utilisateur**: User base class with inheritance
- **Media**: Media files (Image, Video)

### User Types
- **Admin**: Administrative users with full access
- **Touriste**: Tourist users
- **Fournisseur**: Service providers
- **AgenceLocation**: Rental agencies
- **Partenaire**: Business partners

### Supporting Entities
- **Pays**: Countries
- **Climat**: Climate types
- **Localisation**: Geographic coordinates
- **Vehicule**: Rental vehicles
- **Produit**: Shop products
- **Paiement**: Payment transactions
- **Avis**: Reviews and ratings

## API Endpoints

### Authentication
```
POST /auth/register - User registration
```

### Admin Endpoints (Requires ADMIN role)

#### User Management
```
GET /admin/users - Get all users
GET /admin/users/search?query={query} - Search users
POST /admin/users/{id}/block - Block user
DELETE /admin/users/{id} - Delete user
GET /admin/users/export-csv - Export users to CSV
```

#### Provider Management
```
GET /admin/providers - Get all providers
POST /admin/providers/{id}/validate - Validate provider
PUT /admin/providers/{id} - Update provider
```

#### Content Management
```
GET /admin/content/villes - Get all cities
POST /admin/content/villes - Create city
PUT /admin/content/villes/{id} - Update city
DELETE /admin/content/villes/{id} - Delete city

GET /admin/content/activites - Get all activities
POST /admin/content/activites - Create activity
PUT /admin/content/activites/{id} - Update activity
DELETE /admin/content/activites/{id} - Delete activity
```

#### Shop Management
```
GET /admin/shop/products - Get all products
PUT /admin/shop/products/{id}/price - Update product price
PUT /admin/shop/products/{id}/stock - Update product stock
PUT /admin/shop/products/{id}/visibility - Update product visibility
```

#### Statistics
```
GET /admin/statistics - Get general statistics
GET /admin/statistics/users - Get user statistics
GET /admin/statistics/revenue - Get revenue statistics
```

#### Transaction Management
```
GET /admin/transactions - Get all transactions
GET /admin/transactions/payments - Get payment history
POST /admin/transactions/{id}/refund - Process refund
GET /admin/transactions/commissions - Get commissions
```

### Tourist Endpoints (Requires USER role)

#### City Exploration
```
GET /tourist/villes - Get all cities
GET /tourist/villes/{id} - Get city by ID
GET /tourist/villes/search?query={query} - Search cities
GET /tourist/villes/plage - Get beach cities
GET /tourist/villes/montagne - Get mountain cities
GET /tourist/villes/desert - Get desert cities
GET /tourist/villes/historique - Get historical cities
GET /tourist/villes/culturelle - Get cultural cities
GET /tourist/villes/moderne - Get modern cities
```

#### Activity Exploration
```
GET /tourist/activites - Get all activities
GET /tourist/activites/{id} - Get activity by ID
GET /tourist/activites/search?query={query} - Search activities
GET /tourist/activites/ville/{villeId} - Get activities by city
GET /tourist/activites/prix?minPrix={min}&maxPrix={max} - Get activities by price range
GET /tourist/activites/duree?maxDuree={minutes} - Get activities by max duration
GET /tourist/activites/saison?saison={season} - Get activities by season
```

## Database Schema

The application uses MySQL with the following key tables:

### Main Tables
- `ville` - Cities with characteristics
- `activite` - Activities and tours
- `monument` - Historical monuments
- `hebergement` - Accommodation options
- `utilisateur` - User base table
- `media` - Media files

### User Tables
- `admin` - Administrative users
- `touriste` - Tourist users
- `fournisseur` - Service providers
- `agence_location` - Rental agencies

### Supporting Tables
- `pays` - Countries
- `climat` - Climate types
- `localisation` - Geographic coordinates
- `vehicule` - Rental vehicles
- `produit` - Shop products
- `paiement` - Payment transactions
- `avis` - Reviews and ratings

## Setup Instructions

1. **Database Setup**
   ```sql
   CREATE DATABASE tourisme;
   ```

2. **Keycloak Setup**
   - Install and configure Keycloak
   - Create realm "tourisme"
   - Create roles: "admin", "user"
   - Configure client with secret

3. **Application Configuration**
   - Update `application.properties` with your database and Keycloak settings
   - Ensure MySQL is running on port 3306
   - Ensure Keycloak is running on port 8090

4. **Run Application**
   ```bash
   mvn spring-boot:run
   ```

## Administrative Tasks

The system supports all six administrative tasks from the requirements:

1. **User Management**: Search, block, delete, export CSV
2. **Provider Management**: Validate and update guides, drivers, hotels
3. **Content Management**: Add/modify activities, monuments, cities
4. **Shop Product Management**: Price, stock, visibility management
5. **Statistics Consultation**: Integration and dynamic display
6. **Transaction Management**: Payment history, refunds, commissions

## Security

- JWT-based authentication via Keycloak
- Role-based access control (ADMIN, USER)
- CORS configuration for Angular frontend
- Secure endpoints with proper authorization

## Technologies Used

- **Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **Database**: MySQL 8.x
- **Authentication**: Keycloak
- **Build Tool**: Maven
- **Java Version**: 17+ 