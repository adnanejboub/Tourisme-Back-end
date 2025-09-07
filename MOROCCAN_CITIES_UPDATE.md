# Mise à Jour des Villes Marocaines

## Vue d'ensemble

Ce document décrit la mise à jour de la base de données pour supprimer les villes non-marocaines et ajouter les nouvelles villes marocaines avec leurs caractéristiques, monuments et activités.

## Changements Effectués

### 1. Suppression des Villes Non-Marocaines
- **Tunis** (Tunisie)
- **Alger** (Algérie) 
- **Le Caire** (Égypte)
- **Istanbul** (Turquie)

### 2. Nouvelles Villes Marocaines Ajoutées

#### 🏖️ **Safi** - Ville Côtière Industrielle
- **Caractéristiques**: Plage, Riviera, Historique, Culturelle, Moderne
- **Climat**: Méditerranéen
- **Monuments**: Kasbah de Safi, Potterie de Safi
- **Activités**: Visite des Poteries, Plage de Safi
- **Note**: 4.2/5

#### 🏛️ **Fès** - Ville Impériale Historique
- **Caractéristiques**: Historique, Culturelle
- **Climat**: Continental
- **Monuments**: Médina de Fès (UNESCO), Université Al Quaraouiyine, Tanneries
- **Activités**: Visite de la Médina, Tanneries de Fès
- **Note**: 4.7/5

#### 🏰 **Meknes** - Ville Impériale
- **Caractéristiques**: Historique, Culturelle
- **Climat**: Continental
- **Monuments**: Bab Mansour, Mausolée Moulay Ismaïl
- **Activités**: Visite de Meknes Impériale, Volubilis
- **Note**: 4.5/5

#### 🏘️ **Tétouan** - Ville Blanche Andalouse
- **Caractéristiques**: Plage, Riviera, Historique, Culturelle
- **Climat**: Méditerranéen
- **Monuments**: Médina de Tétouan (UNESCO), Plage de Martil
- **Activités**: Médina Andalouse, Plage de Martil
- **Note**: 4.3/5

#### 🌍 **Oujda** - Ville de l'Est
- **Caractéristiques**: Historique, Culturelle, Moderne
- **Climat**: Continental
- **Infrastructure**: Aéroport, Gare
- **Note**: 4.1/5

#### 🌊 **Tanger** - Porte de l'Afrique
- **Caractéristiques**: Plage, Riviera, Historique, Culturelle, Moderne
- **Climat**: Méditerranéen
- **Infrastructure**: Aéroport, Gare, Port
- **Monuments**: Kasbah de Tanger, Grotte d'Hercule
- **Activités**: Kasbah de Tanger, Grotte d'Hercule
- **Note**: 4.4/5

#### 🏔️ **Ifrane** - Petite Suisse du Maroc
- **Caractéristiques**: Montagne, Culturelle
- **Climat**: Montagnard
- **Monuments**: Lion de pierre, Forêt de cèdres
- **Activités**: Randonnée en Forêt de Cèdres, Visite d'Ifrane
- **Note**: 4.6/5

#### 🏜️ **Dakhla** - Perle du Désert
- **Caractéristiques**: Plage, Désert, Culturelle, Moderne
- **Climat**: Désertique
- **Infrastructure**: Aéroport
- **Monuments**: Lagon de Dakhla, Désert de Dakhla
- **Activités**: Kitesurf, Excursion dans le Désert
- **Note**: 4.8/5

### 3. Mise à Jour des Villes Existantes

#### **Casablanca**
- Ajout des caractéristiques modernes
- Infrastructure: Aéroport, Port

#### **Rabat**
- Caractéristiques: Historique, Moderne
- Infrastructure: Aéroport

#### **Marrakech**
- Caractéristiques: Historique, Culturelle
- Infrastructure: Aéroport

#### **Agadir**
- Caractéristiques: Plage
- Infrastructure: Aéroport

## Structure de la Base de Données

### Table `ville`
- **Caractéristiques booléennes**: `is_plage`, `is_montagne`, `is_desert`, `is_riviera`, `is_historique`, `is_culturelle`, `is_moderne`
- **Infrastructure**: `has_aeroport`, `has_gare`, `has_port`
- **Géolocalisation**: `latitude`, `longitude`
- **Relations**: `id_pays`, `id_climat`

### Table `monument`
- **Informations**: `nom_monument`, `adresse_monument`, `description`
- **Tarification**: `prix`, `gratuit`
- **Caractéristiques**: `has_culturelle`, `has_historique`
- **Évaluation**: `notes_moyennes`
- **Type**: `type_monument`
- **Image**: `image_url`

### Table `activite`
- **Informations**: `nom`, `description`
- **Tarification**: `prix`
- **Durée**: `duree_minimun`, `duree_maximun`
- **Saisonnalité**: `saison`
- **Difficulté**: `niveau_dificulta`
- **Catégorie**: `categorie`
- **Image**: `image_url`

## Exécution du Script

### Méthode 1: Script Batch (Windows)
```bash
run_city_update.bat
```

### Méthode 2: Exécution Manuelle
1. Ouvrir la base de données H2
2. Copier le contenu de `update_moroccan_cities.sql`
3. Exécuter le script

### Méthode 3: Via Application
1. Démarrer l'application Spring Boot
2. Accéder à la console H2: `http://localhost:8080/h2-console`
3. Exécuter le script SQL

## Vérification

Après l'exécution, vérifiez que:
1. Les villes non-marocaines ont été supprimées
2. Les nouvelles villes marocaines sont présentes
3. Les monuments et activités sont associés aux bonnes villes
4. Les caractéristiques et notes sont correctes

## API Endpoints

Les nouvelles villes seront disponibles via:
- `GET /public/cities` - Liste de toutes les villes
- `GET /public/cities/{id}` - Détails d'une ville
- `GET /public/cities/{id}/details` - Détails complets avec monuments et activités
- `GET /public/cities/{id}/monuments` - Monuments d'une ville
- `GET /public/cities/{id}/activities` - Activités d'une ville

## Images

Les images des villes sont gérées par le service `ImageService` dans l'application Flutter:
- Images locales dans `assets/images/cities/`
- Fallback automatique vers des images représentatives
- Support des images réseau et locales

## Notes Importantes

1. **Sauvegarde**: Effectuez une sauvegarde de la base de données avant l'exécution
2. **Test**: Testez l'application après la mise à jour
3. **Images**: Remplacez les placeholders par de vraies images
4. **Performance**: Les nouvelles données peuvent affecter les performances de recherche

## Support

Pour toute question ou problème:
1. Vérifiez les logs de l'application
2. Consultez la documentation de l'API
3. Testez les endpoints individuellement
