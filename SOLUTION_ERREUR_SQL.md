# 🔧 Solution pour l'Erreur SQL - Contraintes de Clés Étrangères

## ❌ Problème Rencontré

```
#1451 - Cannot delete or update a parent row: a foreign key constraint fails 
(`tourisme`.`monument`, CONSTRAINT `FK1kj6gu2p6x5owojljood8chr9` 
FOREIGN KEY (`id_ville`) REFERENCES `ville` (`id_ville`))
```

## 🔍 Explication du Problème

L'erreur indique que vous ne pouvez pas supprimer les villes car il y a des **contraintes de clés étrangères**. Les tables `monument`, `activite`, `hebergement`, et `service` sont liées à la table `ville` par des clés étrangères.

**Ordre de suppression requis :**
1. D'abord supprimer les **données dépendantes** (monuments, activités, etc.)
2. Ensuite supprimer les **villes**

## ✅ Solutions Disponibles

### Solution 1 : Script SQL Corrigé (Recommandé)

Utilisez le fichier `update_moroccan_cities.sql` qui a été corrigé :

```sql
-- 1. Supprimer les monuments associés aux villes non-marocaines
DELETE FROM monument WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- 2. Supprimer les activités associées
DELETE FROM activite WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- 3. Supprimer les hébergements associés
DELETE FROM hebergement WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- 4. Supprimer les services associés
DELETE FROM service WHERE id_ville IN (
    SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey')
);

-- 5. Maintenant supprimer les villes
DELETE FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul', 'Cairo', 'Tunisia', 'Algeria', 'Egypt', 'Turkey');
```

### Solution 2 : Script SQL SÉCURISÉ (Alternative)

Utilisez le fichier `update_moroccan_cities_safe.sql` qui :
- Désactive temporairement les vérifications de clés étrangères
- Utilise des transactions pour la sécurité
- Utilise `INSERT IGNORE` pour éviter les doublons

### Solution 3 : Suppression Manuelle (Si nécessaire)

Si les scripts automatiques ne fonctionnent pas, vous pouvez supprimer manuellement :

```sql
-- 1. Vérifier quelles villes existent
SELECT id_ville, nom_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul');

-- 2. Supprimer les monuments (remplacez X par l'ID de la ville)
DELETE FROM monument WHERE id_ville = X;

-- 3. Supprimer les activités
DELETE FROM activite WHERE id_ville = X;

-- 4. Supprimer les hébergements
DELETE FROM hebergement WHERE id_ville = X;

-- 5. Supprimer les services
DELETE FROM service WHERE id_ville = X;

-- 6. Supprimer la ville
DELETE FROM ville WHERE id_ville = X;
```

## 🚀 Instructions d'Exécution

### Méthode 1 : Via l'Interface H2
1. Ouvrez votre application Spring Boot
2. Accédez à la console H2 : `http://localhost:8080/h2-console`
3. Connectez-vous à votre base de données
4. Copiez et exécutez le contenu de `update_moroccan_cities.sql`

### Méthode 2 : Via MySQL/PostgreSQL
```bash
# Pour MySQL
mysql -u username -p database_name < update_moroccan_cities.sql

# Pour PostgreSQL
psql -U username -d database_name -f update_moroccan_cities.sql
```

### Méthode 3 : Script Sécurisé
```bash
# Exécuter le script sécurisé
mysql -u username -p database_name < update_moroccan_cities_safe.sql
```

## 🔍 Vérification

Après l'exécution, vérifiez que :

```sql
-- 1. Les villes non-marocaines ont été supprimées
SELECT * FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul');

-- 2. Les nouvelles villes marocaines sont présentes
SELECT nom_ville, description FROM ville WHERE id_pays = (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc');

-- 3. Les monuments sont associés aux bonnes villes
SELECT v.nom_ville, m.nom_monument FROM ville v 
JOIN monument m ON v.id_ville = m.id_ville 
WHERE v.id_pays = (SELECT id_pays FROM pays WHERE nom_pays = 'Maroc');
```

## ⚠️ Précautions

1. **Sauvegarde** : Effectuez une sauvegarde de votre base de données avant l'exécution
2. **Test** : Testez d'abord sur une base de données de test
3. **Vérification** : Vérifiez les résultats après l'exécution
4. **Rollback** : Gardez une copie de vos données originales

## 🆘 En Cas de Problème

Si vous rencontrez encore des erreurs :

1. **Vérifiez les contraintes** :
```sql
SHOW CREATE TABLE monument;
SHOW CREATE TABLE activite;
SHOW CREATE TABLE hebergement;
SHOW CREATE TABLE service;
```

2. **Vérifiez les données existantes** :
```sql
SELECT COUNT(*) FROM monument WHERE id_ville IN (SELECT id_ville FROM ville WHERE nom_ville IN ('Tunis', 'Alger', 'Le Caire', 'Istanbul'));
```

3. **Contactez le support** avec les messages d'erreur complets

## 📋 Checklist de Résolution

- [ ] Sauvegarde de la base de données effectuée
- [ ] Script SQL corrigé utilisé
- [ ] Suppression des données dépendantes réussie
- [ ] Suppression des villes réussie
- [ ] Ajout des nouvelles villes marocaines réussi
- [ ] Vérification des résultats effectuée
- [ ] Application testée

---

**💡 Conseil** : Utilisez toujours le script `update_moroccan_cities_safe.sql` pour éviter ce type de problème à l'avenir !
