-- Script de test pour vérifier la structure de la table activite
-- Exécutez ce script pour voir la structure exacte de votre table

-- 1. Vérifier la structure de la table activite
DESCRIBE activite;

-- 2. Vérifier les contraintes de la table
SHOW CREATE TABLE activite;

-- 3. Vérifier les valeurs possibles pour les catégories
SELECT DISTINCT categorie FROM activite ORDER BY categorie;

-- 4. Vérifier les valeurs possibles pour les niveaux de difficulté
SELECT DISTINCT niveau_dificulta FROM activite ORDER BY niveau_dificulta;

-- 5. Vérifier les valeurs possibles pour les saisons
SELECT DISTINCT saison FROM activite ORDER BY saison;

-- 6. Tester un INSERT simple
INSERT INTO activite (nom, duree_minimun, duree_maximun, saison, niveau_dificulta, categorie, conditions_speciales, id_ville) 
VALUES ('Test Activité', 60, 120, 'Toute l''année', 'Facile', 'TOURS', 'Test condition', 1);

-- 7. Vérifier que l'insertion a fonctionné
SELECT * FROM activite WHERE nom = 'Test Activité';

-- 8. Supprimer l'activité de test
DELETE FROM activite WHERE nom = 'Test Activité';

-- 9. Afficher le message de succès
SELECT 'Structure de la table activite vérifiée avec succès' as message;
