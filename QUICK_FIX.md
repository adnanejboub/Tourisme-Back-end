# Résolution Rapide - Erreur "Column 'nom_agence' cannot be null"

## 🚨 Problème Identifié
L'erreur indique que la base de données existante a encore des contraintes NOT NULL sur les colonnes des sous-classes, même si nous les avons marquées comme `nullable = true` dans le code Java.

## ✅ Solution Immédiate

### Option 1: Script Automatique (Recommandé)
```bash
# Windows
start-clean.bat

# Linux/Mac
chmod +x start-clean.sh
./start-clean.sh
```

### Option 2: Manuel
1. **Arrêter l'application Spring Boot** (Ctrl+C)
2. **Nettoyer la base de données** :
   ```bash
   mysql -u root -p < clean-database.sql
   ```
3. **Redémarrer l'application** :
   ```bash
   mvn spring-boot:run
   ```

## 🔧 Pourquoi Cette Solution Fonctionne

1. **`spring.jpa.hibernate.ddl-auto=create-drop`** : Force la recréation des tables
2. **Suppression des anciennes tables** : Élimine les contraintes NOT NULL existantes
3. **Nouvelle structure** : JPA crée les tables avec `nullable = true` sur toutes les colonnes des sous-classes

## 📋 Vérification

Après le redémarrage, vérifiez dans les logs :
- ✅ "Database connection successful!"
- ✅ "Table 'utilisateur' not found yet (will be created by JPA)"
- ✅ Tables créées avec la bonne structure

## 🧪 Test de l'Endpoint

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "touriste1",
    "email": "touriste1@example.com",
    "password": "password123",
    "firstName": "Jean",
    "lastName": "Dupont"
  }'
```

**Résultat attendu** : Inscription réussie sans erreur de contrainte NOT NULL.

## ⚠️ Important

- **Données perdues** : Cette solution supprime toutes les données existantes
- **Recréation** : Les tables sont recréées à chaque démarrage (mode développement)
- **Production** : Changez `ddl-auto` à `validate` après la première création

## 🔄 Pour la Production

Une fois les tables créées correctement, modifiez `application.properties` :
```properties
spring.jpa.hibernate.ddl-auto=validate
```
