-- Script de diagnostic pour vérifier les doublons dans la base de données
-- Exécutez ce script AVANT d'exécuter le script principal

-- 1. Vérifier les doublons de villes
SELECT nom_ville, COUNT(*) as nombre_doublons
FROM ville 
GROUP BY nom_ville 
HAVING COUNT(*) > 1
ORDER BY nombre_doublons DESC;

-- 2. Vérifier les villes non-marocaines existantes
SELECT nom_ville, id_ville, description
FROM ville 
WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
ORDER BY nom_ville;

-- 3. Vérifier les monuments liés aux villes non-marocaines
SELECT m.nom_monument, v.nom_ville, m.id_ville
FROM monument m
JOIN ville v ON m.id_ville = v.id_ville
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
ORDER BY v.nom_ville;

-- 4. Vérifier les activités liées aux villes non-marocaines
SELECT a.nom, v.nom_ville, a.id_ville
FROM activite a
JOIN ville v ON a.id_ville = v.id_ville
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
ORDER BY v.nom_ville;

-- 5. Vérifier les hébergements liés aux villes non-marocaines
SELECT h.nom_hebergement, v.nom_ville, h.id_ville
FROM hebergement h
JOIN ville v ON h.id_ville = v.id_ville
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
ORDER BY v.nom_ville;

-- 6. Vérifier les services liés aux villes non-marocaines
SELECT s.nom_service, v.nom_ville, s.id_ville
FROM service s
JOIN ville v ON s.id_ville = v.id_ville
WHERE v.nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
ORDER BY v.nom_ville;

-- 7. Vérifier si le Maroc existe dans la table pays
SELECT * FROM pays WHERE nom_pays = 'Maroc' OR code_pays = 'MA';

-- 8. Vérifier les climats existants
SELECT * FROM climat ORDER BY nom_climat;

-- 9. Compter le nombre total de villes
SELECT COUNT(*) as total_villes FROM ville;

-- 10. Afficher toutes les villes marocaines existantes
SELECT nom_ville, id_ville, description, is_plage, is_historique, is_culturelle, is_moderne
FROM ville 
WHERE id_pays = (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc' OR code_pays = 'MA')
ORDER BY nom_ville;
