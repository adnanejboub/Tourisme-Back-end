@echo off
echo Mise à jour des villes marocaines...
echo.

REM Vérifier si le fichier SQL existe
if not exist "update_moroccan_cities.sql" (
    echo Erreur: Le fichier update_moroccan_cities.sql n'existe pas
    pause
    exit /b 1
)

echo Exécution du script SQL...
echo.

REM Exécuter le script SQL (remplacez par votre commande de base de données)
REM Exemple pour PostgreSQL:
REM psql -h localhost -U username -d database_name -f update_moroccan_cities.sql

REM Exemple pour MySQL:
REM mysql -h localhost -u username -p database_name < update_moroccan_cities.sql

REM Pour H2 (base de données en mémoire), vous devrez exécuter le script manuellement
echo Pour exécuter le script SQL:
echo 1. Ouvrez votre base de données H2
echo 2. Copiez le contenu de update_moroccan_cities.sql
echo 3. Exécutez le script
echo.

echo Script SQL créé avec succès!
echo Fichier: update_moroccan_cities.sql
echo.
echo Le script contient:
echo - Suppression des villes non-marocaines
echo - Ajout des nouvelles villes marocaines
echo - Ajout des monuments pour chaque ville
echo - Ajout des activités pour chaque ville
echo.

pause
