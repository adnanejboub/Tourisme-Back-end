-- Script SQL SÉCURISÉ pour mettre à jour les villes marocaines
-- Utilise des transactions pour éviter les problèmes de contraintes

-- Désactiver temporairement les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS = 0;

-- Commencer une transaction
START TRANSACTION;

-- 1. Supprimer les villes non-marocaines et toutes leurs données associées
-- Supprimer les monuments
DELETE m FROM monument m 
INNER JOIN ville v ON m.id_ville = v.id_ville 
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey');

-- Supprimer les activités
DELETE a FROM activite a 
INNER JOIN ville v ON a.id_ville = v.id_ville 
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey');

-- Supprimer les hébergements
DELETE h FROM hebergement h 
INNER JOIN ville v ON h.id_ville = v.id_ville 
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey');

-- Supprimer les services
DELETE s FROM service s 
INNER JOIN ville v ON s.id_ville = v.id_ville 
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey');

-- Supprimer les villes
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

-- 4. Ajouter les nouvelles villes marocaines
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
-- Safi
(
    'Safi',
    'Ville côtière du Maroc connue pour son industrie de la poterie et ses plages. Capitale de la céramique marocaine avec un riche patrimoine artisanal.',
    32.2994,
    -9.2372,
    true, false, false, true, true, true, true,
    false, true, true, true, false, false, true, true, true, true,
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Méditerranéen'),
    4.2
),
-- Fès
(
    'Fès',
    'Ville impériale du Maroc, capitale spirituelle et culturelle. Abrite la plus ancienne université du monde et une médina classée au patrimoine de l''UNESCO.',
    34.0331,
    -5.0003,
    false, false, false, false, true, true, false,
    true, true, false, false, false, false, false, true, true, false,
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Continental'),
    4.7
),
-- Meknes
(
    'Meknes',
    'Ville impériale du Maroc, ancienne capitale sous le règne de Moulay Ismaïl. Connue pour ses monuments historiques et ses portes majestueuses.',
    33.8935,
    -5.5473,
    false, false, false, false, true, true, false,
    false, true, false, false, false, false, false, true, true, false,
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Continental'),
    4.5
),
-- Tétouan
(
    'Tétouan',
    'Ville blanche du nord du Maroc, influencée par l''architecture andalouse. Porte d''entrée vers l''Espagne et mélange unique de cultures.',
    35.5889,
    -5.3626,
    true, false, false, true, true, true, false,
    false, true, false, true, false, false, true, true, true, false,
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Méditerranéen'),
    4.3
),
-- Oujda
(
    'Oujda',
    'Ville de l''est du Maroc, proche de la frontière algérienne. Centre commercial et culturel de la région de l''Oriental.',
    34.6814,
    -1.9086,
    false, false, false, false, true, true, true,
    true, true, false, false, false, false, false, true, true, true,
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Continental'),
    4.1
),
-- Tanger
(
    'Tanger',
    'Porte de l''Afrique, ville cosmopolite du nord du Maroc. Point de rencontre entre l''Europe et l''Afrique, riche en histoire et culture.',
    35.7595,
    -5.8340,
    true, false, false, true, true, true, true,
    true, true, true, true, false, false, true, true, true, true,
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Méditerranéen'),
    4.4
),
-- Ifrane
(
    'Ifrane',
    'Petite Suisse du Maroc, station de montagne à 1650m d''altitude. Architecture européenne, forêts de cèdres et climat frais toute l''année.',
    33.5333,
    -5.1167,
    false, true, false, false, false, true, false,
    false, false, false, false, true, false, false, false, true, false,
    (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc'),
    (SELECT id_climat FROM climat WHERE nom_climat = 'Montagnard'),
    4.6
),
-- Dakhla
(
    'Dakhla',
    'Perle du désert au sud du Maroc, célèbre pour son lagon et ses sports nautiques. Destination unique combinant désert et océan.',
    23.6847,
    -15.9580,
    true, false, true, false, false, true, true,
    true, false, false, true, false, true, false, false, true, true,
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
INSERT IGNORE INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Kasbah de Safi', 'Kasbah, Safi', 'Ancienne forteresse portugaise dominant la ville', 0, true, true, true, 4.5, 'Fortification', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Potterie de Safi', 'Quartier des Potiers, Safi', 'Centre de production de céramique traditionnelle', 20, false, true, false, 4.3, 'Artisanat', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Fès
INSERT IGNORE INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Médina de Fès', 'Fès el-Bali, Fès', 'Médina classée au patrimoine mondial de l''UNESCO', 0, true, true, true, 4.8, 'Médina', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès'), 'https://images.unsplash.com/photo-1517685352821-92cf88aee5a5'),
('Université Al Quaraouiyine', 'Fès el-Bali, Fès', 'Plus ancienne université du monde', 0, true, true, true, 4.9, 'Université', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Tanneries de Fès', 'Fès el-Bali, Fès', 'Tanneries traditionnelles en activité', 0, true, true, true, 4.6, 'Artisanat', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Meknes
INSERT IGNORE INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Bab Mansour', 'Place el-Hedim, Meknes', 'Porte monumentale de Meknes', 0, true, true, true, 4.7, 'Porte', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Mausolée Moulay Ismaïl', 'Meknes', 'Tombeau du sultan Moulay Ismaïl', 0, true, true, true, 4.5, 'Mausolée', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Tétouan
INSERT IGNORE INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Médina de Tétouan', 'Tétouan', 'Médina andalouse classée au patrimoine mondial', 0, true, true, true, 4.4, 'Médina', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan'), 'https://images.unsplash.com/photo-1517685352821-92cf88aee5a5'),
('Plage de Martil', 'Martil, Tétouan', 'Plage méditerranéenne populaire', 0, true, false, false, 4.2, 'Plage', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4');

-- Tanger
INSERT IGNORE INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Kasbah de Tanger', 'Tanger', 'Ancienne forteresse avec vue sur le détroit', 0, true, true, true, 4.6, 'Fortification', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Grotte d''Hercule', 'Cap Spartel, Tanger', 'Grotte légendaire avec ouverture sur la mer', 10, false, true, true, 4.3, 'Site Naturel', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Ifrane
INSERT IGNORE INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Lion de pierre d''Ifrane', 'Centre-ville, Ifrane', 'Symbole de la ville sculpté dans la pierre', 0, true, true, false, 4.5, 'Sculpture', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Forêt de cèdres d''Ifrane', 'Ifrane', 'Forêt de cèdres de l''Atlas', 0, true, false, false, 4.7, 'Nature', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Dakhla
INSERT IGNORE INTO monument (nom_monument, adresse_monument, description, prix, gratuit, has_culturelle, has_historique, notes_moyennes, type_monument, id_ville, image_url) VALUES
('Lagon de Dakhla', 'Dakhla', 'Lagon unique au monde pour les sports nautiques', 0, true, false, false, 4.9, 'Site Naturel', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Désert de Dakhla', 'Dakhla', 'Dunes de sable et paysages désertiques', 0, true, false, false, 4.8, 'Désert', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- 6. Ajouter des activités pour chaque ville
-- Safi
INSERT IGNORE INTO activite (nom, description, prix, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, id_ville, image_url) VALUES
('Visite des Poteries de Safi', 'Découverte des techniques de poterie traditionnelle', 50, 120, 180, 'Toute l''année', 'Facile', 'Culture', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144'),
('Plage de Safi', 'Détente sur les plages de la côte atlantique', 0, 60, 480, 'Été', 'Facile', 'Plage', (SELECT id_ville FROM ville WHERE nom_ville = 'Safi'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4');

-- Fès
INSERT IGNORE INTO activite (nom, description, prix, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, id_ville, image_url) VALUES
('Visite de la Médina de Fès', 'Exploration de la médina classée UNESCO', 100, 180, 300, 'Toute l''année', 'Moyen', 'Culture', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès'), 'https://images.unsplash.com/photo-1517685352821-92cf88aee5a5'),
('Tanneries de Fès', 'Découverte des tanneries traditionnelles', 30, 60, 120, 'Toute l''année', 'Facile', 'Artisanat', (SELECT id_ville FROM ville WHERE nom_ville = 'Fès'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Meknes
INSERT IGNORE INTO activite (nom, description, prix, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, id_ville, image_url) VALUES
('Visite de Meknes Impériale', 'Découverte des monuments de la ville impériale', 80, 150, 240, 'Toute l''année', 'Facile', 'Histoire', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Volubilis depuis Meknes', 'Excursion vers le site archéologique romain', 150, 240, 360, 'Printemps, Été, Automne', 'Moyen', 'Archéologie', (SELECT id_ville FROM ville WHERE nom_ville = 'Meknes'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Tétouan
INSERT IGNORE INTO activite (nom, description, prix, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, id_ville, image_url) VALUES
('Médina Andalouse de Tétouan', 'Visite de la médina andalouse', 60, 120, 180, 'Toute l''année', 'Facile', 'Culture', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan'), 'https://images.unsplash.com/photo-1517685352821-92cf88aee5a5'),
('Plage de Martil', 'Détente sur la plage méditerranéenne', 0, 60, 480, 'Été', 'Facile', 'Plage', (SELECT id_ville FROM ville WHERE nom_ville = 'Tétouan'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4');

-- Tanger
INSERT IGNORE INTO activite (nom, description, prix, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, id_ville, image_url) VALUES
('Kasbah de Tanger', 'Visite de la forteresse historique', 40, 90, 150, 'Toute l''année', 'Facile', 'Histoire', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Grotte d''Hercule', 'Découverte de la grotte légendaire', 20, 60, 120, 'Toute l''année', 'Facile', 'Nature', (SELECT id_ville FROM ville WHERE nom_ville = 'Tanger'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

-- Ifrane
INSERT IGNORE INTO activite (nom, description, prix, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, id_ville, image_url) VALUES
('Randonnée en Forêt de Cèdres', 'Balade dans la forêt de cèdres de l''Atlas', 80, 120, 240, 'Printemps, Été, Automne', 'Moyen', 'Randonnée', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144'),
('Visite d''Ifrane', 'Découverte de la petite Suisse du Maroc', 50, 90, 180, 'Toute l''année', 'Facile', 'Culture', (SELECT id_ville FROM ville WHERE nom_ville = 'Ifrane'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4');

-- Dakhla
INSERT IGNORE INTO activite (nom, description, prix, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, id_ville, image_url) VALUES
('Kitesurf à Dakhla', 'Sports nautiques sur le lagon', 200, 120, 240, 'Toute l''année', 'Difficile', 'Sport', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla'), 'https://images.unsplash.com/photo-1570191913384-b786dde7d9b4'),
('Excursion dans le Désert', 'Balade en chameau dans les dunes', 150, 180, 360, 'Toute l''année', 'Moyen', 'Désert', (SELECT id_ville FROM ville WHERE nom_ville = 'Dakhla'), 'https://images.unsplash.com/photo-1590736969955-71cc94901144');

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

-- Valider la transaction
COMMIT;

-- Réactiver les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;

-- Afficher le résultat
SELECT 'Mise à jour des villes marocaines terminée avec succès!' as message;
SELECT nom_ville, description, is_plage, is_historique, is_culturelle, is_moderne 
FROM ville 
WHERE id_pays = (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc')
ORDER BY nom_ville;
