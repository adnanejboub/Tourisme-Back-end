-- Script de nettoyage pour supprimer les doublons de villes
-- ATTENTION: Exécutez d'abord check_duplicates.sql pour voir les doublons

-- 1. Supprimer les monuments des villes en doublon (garder seulement la première)
DELETE m1 FROM monument m1
INNER JOIN monument m2 
WHERE m1.id_monument > m2.id_monument 
AND m1.id_ville = m2.id_ville;

-- 2. Supprimer les activités des villes en doublon (garder seulement la première)
DELETE a1 FROM activite a1
INNER JOIN activite a2 
WHERE a1.id_activite > a2.id_activite 
AND a1.id_ville = a2.id_ville;

-- 3. Supprimer les hébergements des villes en doublon (garder seulement le premier)
DELETE h1 FROM hebergement h1
INNER JOIN hebergement h2 
WHERE h1.id_hebergement > h2.id_hebergement 
AND h1.id_ville = h2.id_ville;

-- 4. Supprimer les services des villes en doublon (garder seulement le premier)
DELETE s1 FROM service s1
INNER JOIN service s2 
WHERE s1.id_service > s2.id_service 
AND s1.id_ville = s2.id_ville;

-- 5. Supprimer les villes en doublon (garder seulement la première)
DELETE v1 FROM ville v1
INNER JOIN ville v2 
WHERE v1.id_ville > v2.id_ville 
AND v1.nom_ville = v2.nom_ville;

-- 6. Vérifier le résultat
SELECT 'Nettoyage terminé' as message;
SELECT nom_ville, COUNT(*) as nombre_doublons
FROM ville 
GROUP BY nom_ville 
HAVING COUNT(*) > 1
ORDER BY nombre_doublons DESC;
