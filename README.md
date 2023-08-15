# lefort-simon-projet-6
# Pay My Buddy
Pay My Buddy - We make moving your money easy!
Le transfert d'argent entre amis n'a jamais été aussi rapide.

Ceci est le projet n°6 du parcours de formation Développeur Java par OpenClassRooms. L'objectif est de concevoir une application Web de A à Z.

## Guide d'installation

Ce programme utilise les téchnologies suivantes :
- Java 17
- Spring Boot 3.1.1
- Spring Security 6.1.1
- Maven 3.6.2
- Mysql 8.0.17
- Thymeleaf 3.1.1

### Prerequis

Pour installer et lancer ce programme, vous devez disposez de la configuration suivante (ou plus récente) :

- Java 17
- Maven 3.6.2
- Mysql 8.0.17


### Installation

L'installation se fait en quelques étapes :

1.Installation de Java:

https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

2.Installer Maven:

https://maven.apache.org/install.html

3.Installer MySql:

https://dev.mysql.com/downloads/mysql/

Apres l'installation de MySQL, vous devez créer une base de données avec le nom "paymybuddydb" et une base de données avec le nom "paymybuddytest" pour les tests.
Le nom d'utilisateur par défaut est "root". Vous pouvez ajouter votre mot de passe MySQL dans les fichiers application.properties (dans src/main/resources et src/test/resources) ou en variable d'environnement.
Les tables et colonnes sont générées à partir des modèles Java.
Au lancement du programme, un script SQL est éxécuté pour pré remplir les tables avec des valeurs par défaut.

4.Ports

Par défaut, le programme utilise le port 8080 et necessite que la base de données soit sur le port 3306. Si besoin, vous pouvez modifier ces paramètres.

5.Installer le programme

Vous pouvez installer le programme avec :
mvn clean install 

### Lancer le programme

Vous pouvez lancer le programme avec :
mvn spring-boot:run

Lorsque l'instruction " Started PaymybuddyApplication in XX seconds" s'affiche, vous pouvez utiliser votre navigateur web pour accéder à :
http://localhost:8080/paymybuddy/login

Pour simplifier la découverte du programme, l'écran de login est pré-rempli avec les identifiants d'un compte avec une liste de contacts et 50.000€ de crédit.

### Ressources
Le dépot GitHub contient tous les éléments.

* Base de données :
Le script "AlimentationDB.sql" qui pré rempli les tables est disponible dans :
src/main/resources

* Front :
Le front est fait en HTML et CSS avec Thymeleaf. Vous pouvez retrouver les fichiers :
- CSS : src/main/resources/static
- HTML : src/main/resources/templates
- images : src/main/resources/public

* Schemas :
- Diagramme UML : src\main\resources\Diagramme UML PMB final.pdf
- Modèle physique de données : src\main\resources\ER Diagram.png
