-- Script SQL pour mettre à jour les villes marocaines
-- Supprimer les villes non-marocaines et ajouter les nouvelles villes marocaines

-- 1. Supprimer les villes non-marocaines (en respectant les contraintes de clés étrangères)
-- D'abord, supprimer les monuments associés aux villes non-marocaines
DELETE FROM monument WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- Ensuite, supprimer les activités associées aux villes non-marocaines
DELETE FROM activite WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- Supprimer les hébergements associés aux villes non-marocaines
DELETE FROM hebergement WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- Supprimer les services associés aux villes non-marocaines
DELETE FROM service WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- Maintenant, supprimer les villes non-marocaines
DELETE FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey');

-- 2. Vérifier que le Maroc existe dans la table pays
INSERT IGNORE INTO pays (nom_pays, code_pays) VALUES ('Maroc', 'MA');

-- 3. Vérifier que les climats existent
INSERT IGNORE INTO climat (nom_climat, description) VALUES 
('Méditerranéen', 'Climat méditerranéen avec étés chauds et hivers doux'),
('Océanique', 'Climat océanique avec températures modérées'),
('Continental', 'Climat continental avec variations de température'),
('Désertique', 'Climat désertique avec très peu de précipitations'),
('Montagnard', 'Climat montagnard avec hivers froids et étés frais');

-- 4. Ajouter les nouvelles villes marocaines avec leurs caractéristiques
INSERT INTO ville (
    nom_ville, 
    description, 
    latitude, 
    longitude, 
    is_plage, 
    is_montagne, 
    is_desert, 
    is_riviera, 
    is_historique, 
    is_culturelle, 
    is_moderne,
    has_aeroport,
    has_gare,
    has_port,
    has_plage,
    has_montagne,
    has_desert,
    has_riviera,
    has_historique,
    has_culturelle,
    has_moderne,
    id_pays,
    id_climat,
    note_moyenne
) VALUES 
(
    'Safi',
    'Ville côtière du Maroc connue pour son industrie de la poterie et ses plages. Capitale de la céramique marocaine avec un riche patrimoine artisanal.',
    32.2994,
    -9.2372,
    true,   -- is_plage
    false,  -- is_montagne
    false,  -- is_desert
    true,   -- is_riviera
    true,   -- is_historique
    true,   -- is_culturelle
    true,   -- is_moderne
    false,  -- has_aeroport
    true,   -- has_gare
    true,   -- has_port
    true,   -- has_plage
    false,  -- has_montagne
    false,  -- has_desert
    true,   -- has_riviera
    true,   -- has_historique
    true,   -- has_culturelle
    true,   -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Méditerranéen'),
    4.2
),

-- Fès - Ville impériale historique
(
    'Fès',
    'Ville impériale du Maroc, capitale spirituelle et culturelle. Abrite la plus ancienne université du monde et une médina classée au patrimoine de l''UNESCO.',
    34.0331,
    -5.0003,
    false,  -- is_plage
    false,  -- is_montagne
    false,  -- is_desert
    false,  -- is_riviera
    true,   -- is_historique
    true,   -- is_culturelle
    false,  -- is_moderne
    true,   -- has_aeroport
    true,   -- has_gare
    false,  -- has_port
    false,  -- has_plage
    false,  -- has_montagne
    false,  -- has_desert
    false,  -- has_riviera
    true,   -- has_historique
    true,   -- has_culturelle
    false,  -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Continental'),
    4.7
),

-- Meknes - Ville impériale
(
    'Meknes',
    'Ville impériale du Maroc, ancienne capitale sous le règne de Moulay Ismaïl. Connue pour ses monuments historiques et ses portes majestueuses.',
    33.8935,
    -5.5473,
    false,  -- is_plage
    false,  -- is_montagne
    false,  -- is_desert
    false,  -- is_riviera
    true,   -- is_historique
    true,   -- is_culturelle
    false,  -- is_moderne
    false,  -- has_aeroport
    true,   -- has_gare
    false,  -- has_port
    false,  -- has_plage
    false,  -- has_montagne
    false,  -- has_desert
    false,  -- has_riviera
    true,   -- has_historique
    true,   -- has_culturelle
    false,  -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Continental'),
    4.5
),

-- Tetouan - Ville blanche andalouse
(
    'Tétouan',
    'Ville blanche du nord du Maroc, influencée par l''architecture andalouse. Porte d''entrée vers l''Espagne et mélange unique de cultures.',
    35.5889,
    -5.3626,
    true,   -- is_plage
    false,  -- is_montagne
    false,  -- is_desert
    true,   -- is_riviera
    true,   -- is_historique
    true,   -- is_culturelle
    false,  -- is_moderne
    false,  -- has_aeroport
    true,   -- has_gare
    false,  -- has_port
    true,   -- has_plage
    false,  -- has_montagne
    false,  -- has_desert
    true,   -- has_riviera
    true,   -- has_historique
    true,   -- has_culturelle
    false,  -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Méditerranéen'),
    4.3
),

-- Oujda - Ville de l'est
(
    'Oujda',
    'Ville de l''est du Maroc, proche de la frontière algérienne. Centre commercial et culturel de la région de l''Oriental.',
    34.6814,
    -1.9086,
    false,  -- is_plage
    false,  -- is_montagne
    false,  -- is_desert
    false,  -- is_riviera
    true,   -- is_historique
    true,   -- is_culturelle
    true,   -- is_moderne
    true,   -- has_aeroport
    true,   -- has_gare
    false,  -- has_port
    false,  -- has_plage
    false,  -- has_montagne
    false,  -- has_desert
    false,  -- has_riviera
    true,   -- has_historique
    true,   -- has_culturelle
    true,   -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Continental'),
    4.1
),

-- Tanger - Porte de l'Afrique
(
    'Tanger',
    'Porte de l''Afrique, ville cosmopolite du nord du Maroc. Point de rencontre entre l''Europe et l''Afrique, riche en histoire et culture.',
    35.7595,
    -5.8340,
    true,   -- is_plage
    false,  -- is_montagne
    false,  -- is_desert
    true,   -- is_riviera
    true,   -- is_historique
    true,   -- is_culturelle
    true,   -- is_moderne
    true,   -- has_aeroport
    true,   -- has_gare
    true,   -- has_port
    true,   -- has_plage
    false,  -- has_montagne
    false,  -- has_desert
    true,   -- has_riviera
    true,   -- has_historique
    true,   -- has_culturelle
    true,   -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Méditerranéen'),
    4.4
),

-- Ifrane - Petite Suisse du Maroc
(
    'Ifrane',
    'Petite Suisse du Maroc, station de montagne à 1650m d''altitude. Architecture européenne, forêts de cèdres et climat frais toute l''année.',
    33.5333,
    -5.1167,
    false,  -- is_plage
    true,   -- is_montagne
    false,  -- is_desert
    false,  -- is_riviera
    false,  -- is_historique
    true,   -- is_culturelle
    false,  -- is_moderne
    false,  -- has_aeroport
    false,  -- has_gare
    false,  -- has_port
    false,  -- has_plage
    true,   -- has_montagne
    false,  -- has_desert
    false,  -- has_riviera
    false,  -- has_historique
    true,   -- has_culturelle
    false,  -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Montagnard'),
    4.6
),

-- Dakhla - Perle du désert
(
    'Dakhla',
    'Perle du désert au sud du Maroc, célèbre pour son lagon et ses sports nautiques. Destination unique combinant désert et océan.',
    23.6847,
    -15.9580,
    true,   -- is_plage
    false,  -- is_montagne
    true,   -- is_desert
    false,  -- is_riviera
    false,  -- is_historique
    true,   -- is_culturelle
    true,   -- is_moderne
    true,   -- has_aeroport
    false,  -- has_gare
    false,  -- has_port
    true,   -- has_plage
    false,  -- has_montagne
    true,   -- has_desert
    false,  -- has_riviera
    false,  -- has_historique
    true,   -- has_culturelle
    true,   -- has_moderne
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Désertique'),
    4.8
)

ON DUPLICATE KEY UPDATE
    description = VALUES(description),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    is_plage = VALUES(is_plage),
    is_montagne = VALUES(is_montagne),
    is_desert = VALUES(is_desert),
    is_riviera = VALUES(is_riviera),
    is_historique = VALUES(is_historique),
    is_culturelle = VALUES(is_culturelle),
    is_moderne = VALUES(is_moderne),
    has_aeroport = VALUES(has_aeroport),
    has_gare = VALUES(has_gare),
    has_port = VALUES(has_port),
    has_plage = VALUES(has_plage),
    has_montagne = VALUES(has_montagne),
    has_desert = VALUES(has_desert),
    has_riviera = VALUES(has_riviera),
    has_historique = VALUES(has_historique),
    has_culturelle = VALUES(has_culturelle),
    has_moderne = VALUES(has_moderne),
    id_pays = VALUES(id_pays),
    id_climat = VALUES(id_climat),
    note_moyenne = VALUES(note_moyenne);

-- 5. Ajouter des monuments pour chaque ville
-- Safi
INSERT INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Kasbah de Safi', 'Kasbah, Safi', 'Ancienne forteresse portugaise dominant la ville', 0, true, true, true, 4.5, 'Fortification', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi' LIMIT 1), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Potterie de Safi', 'Quartier des Potiers, Safi', 'Centre de production de céramique traditionnelle', 20, false, true, false, 4.3, 'Artisanat', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi' LIMIT 1), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Fès
INSERT INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Médina de Fès', 'Fès el-Bali, Fès', 'Médina classée au patrimoine mondial de l''UNESCO', 0, true, true, true, 4.8, 'Médina', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès' LIMIT 1), 'https://images.unsplash.com/photo-1517685352821-92cf88aee5a5'),
('Université Al Quaraouiyine', 'Fès el-Bali, Fès', 'Plus ancienne université du monde', 0, true, true, true, 4.9, 'Université', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès' LIMIT 1), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Tanneries de Fès', 'Fès el-Bali, Fès', 'Tanneries traditionnelles en activité', 0, true, true, true, 4.6, 'Artisanat', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès' LIMIT 1), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Meknes
INSERT INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Bab Mansour', 'Place el-Hedim, Meknes', 'Porte monumentale de Meknes', 0, true, true, true, 4.7, 'Porte', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes' LIMIT 1), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Mausolée Moulay Ismaïl', 'Meknes', 'Tombeau du sultan Moulay Ismaïl', 0, true, true, true, 4.5, 'Mausolée', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes' LIMIT 1), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Tétouan
INSERT INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Médina de Tétouan', 'Tétouan', 'Médina andalouse classée au patrimoine mondial', 0, true, true, true, 4.4, 'Médina', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan' LIMIT 1), 'https://images.unsplash.com/photo-1517685352821-92cf88aee5a5'),
('Plage de Martil', 'Martil, Tétouan', 'Plage méditerranéenne populaire', 0, true, false, false, 4.2, 'Plage', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan' LIMIT 1), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4');

-- Tanger
INSERT INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Kasbah de Tanger', 'Tanger', 'Ancienne forteresse avec vue sur le détroit', 0, true, true, true, 4.6, 'Fortification', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger' LIMIT 1), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Grotte d''Hercule', 'Cap Spartel, Tanger', 'Grotte légendaire avec ouverture sur la mer', 10, false, true, true, 4.3, 'Site Naturel', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger' LIMIT 1), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Ifrane
INSERT INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Lion de pierre d''Ifrane', 'Centre-ville, Ifrane', 'Symbole de la ville sculpté dans la pierre', 0, true, true, false, 4.5, 'Sculpture', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane' LIMIT 1), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Forêt de cèdres d''Ifrane', 'Ifrane', 'Forêt de cèdres de l''Atlas', 0, true, false, false, 4.7, 'Nature', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane' LIMIT 1), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Dakhla
INSERT INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Lagon de Dakhla', 'Dakhla', 'Lagon unique au monde pour les sports nautiques', 0, true, false, false, 4.9, 'Site Naturel', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla' LIMIT 1), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Désert de Dakhla', 'Dakhla', 'Dunes de sable et paysages désertiques', 0, true, false, false, 4.8, 'Désert', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla' LIMIT 1), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- 6. Ajouter des activités pour chaque ville
-- Safi
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) VALUES
('Visite des Poteries de Safi', 120, 180, 'Toute l''année', 'Facile', 'CULTURE', 'Guide recommandé', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi' LIMIT 1)),
('Plage de Safi', 60, 480, 'Été', 'Facile', 'PLAGE', 'Équipement de plage', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi' LIMIT 1));

-- Fès
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) VALUES
('Visite de la Médina de Fès', 180, 300, 'Toute l''année', 'Moyen', 'TOURS', 'Guide recommandé', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès' LIMIT 1)),
('Tanneries de Fès', 60, 120, 'Toute l''année', 'Facile', 'CULTURE', 'Meilleur moment: matin', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès' LIMIT 1));

-- Meknes
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) VALUES
('Visite de Meknes Impériale', 150, 240, 'Toute l''année', 'Facile', 'TOURS', 'Guide recommandé', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes' LIMIT 1)),
('Volubilis depuis Meknes', 240, 360, 'Printemps/Automne', 'Moyen', 'TOURS', 'Transport requis', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes' LIMIT 1));

-- Tétouan
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) VALUES
('Médina Andalouse de Tétouan', 120, 180, 'Toute l''année', 'Facile', 'TOURS', 'Guide recommandé', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan' LIMIT 1)),
('Plage de Martil', 60, 480, 'Été', 'Facile', 'PLAGE', 'Équipement de plage', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan' LIMIT 1));

-- Tanger
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) VALUES
('Kasbah de Tanger', 90, 150, 'Toute l''année', 'Facile', 'TOURS', 'Guide recommandé', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger' LIMIT 1)),
('Grotte d''Hercule', 60, 120, 'Toute l''année', 'Facile', 'CULTURE', 'Meilleur moment: coucher de soleil', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger' LIMIT 1));

-- Ifrane
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) VALUES
('Randonnée en Forêt de Cèdres', 120, 240, 'Printemps/Automne', 'Moyen', 'ACTIVITES_PLEIN_AIR', 'Équipement de randonnée', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane' LIMIT 1)),
('Visite d''Ifrane', 90, 180, 'Toute l''année', 'Facile', 'TOURS', 'Vêtements chauds recommandés', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane' LIMIT 1));

-- Dakhla
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) VALUES
('Kitesurf à Dakhla', 120, 240, 'Toute l''année', 'Difficile', 'ACTIVITES_PLEIN_AIR', 'Équipement de surf fourni', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla' LIMIT 1)),
('Excursion dans le Désert', 180, 360, 'Toute l''année', 'Moyen', 'ACTIVITES_PLEIN_AIR', 'Réservation obligatoire', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla' LIMIT 1));

-- 7. Mettre à jour les villes existantes si nécessaire
UPDATE ville SET 
    description = 'Capitale économique du Maroc, ville moderne avec la mosquée Hassan II',
    is_moderne = true,
    has_aeroport = true,
    has_port = true
WHERE nom_ville = 'Casablanca';

UPDATE ville SET 
    description = 'Capitale du Maroc, ville moderne et historique avec la tour Hassan',
    is_historique = true,
    is_moderne = true,
    has_aeroport = true
WHERE nom_ville = 'Rabat';

UPDATE ville SET 
    description = 'Ville impériale du Maroc, célèbre pour sa place Jemaa el-Fnaa et ses souks',
    is_historique = true,
    is_culturelle = true,
    has_aeroport = true
WHERE nom_ville = 'Marrakech';

UPDATE ville SET 
    description = 'Station balnéaire du sud du Maroc, célèbre pour ses plages et son climat',
    is_plage = true,
    has_aeroport = true,
    has_plage = true
WHERE nom_ville = 'Agadir';

-- 8. Afficher le résultat
SELECT 'Mise à jour des villes marocaines terminée' as message;
SELECT nom_ville, description, is_plage, is_historique, is_culturelle, is_moderne 
FROM ville 
WHERE id_pays = (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc')
ORDER BY nom_ville;
