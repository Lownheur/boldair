# Structure du projet Bol d'Air - Application de gestion d'événements sportifs

## Vue d'ensemble
Cette application Spring Boot gère les inscriptions et l'organisation d'événements sportifs "Bol d'Air". Elle permet la gestion des équipes, participants, bénévoles et administrateurs.

## 📁 Structure principale des packages Java

### 🚀 `boldair/StartWebServer.java`
**Point d'entrée principal** de l'application Spring Boot. Lance le serveur web et initialise tous les composants.

---

## 🗄️ **Couche Data (Entités/Modèles)**

### 📊 `boldair/data/`
Contient toutes les entités qui représentent les tables de la base de données :

#### `Compte.java`
- **Rôle** : Gestion des comptes utilisateurs (admin, bénévoles, équipes)
- **Champs** : email, mot de passe, pseudo, rôles (admin/bénévol)
- **Usage** : Authentification et autorisation

#### `Equipe.java`
- **Rôle** : Représente une équipe inscrite à un événement
- **Champs** : nom équipe, catégorie, nombre de tickets repas, statut de paiement
- **Relation** : Liée à une `Epreuve` (id_epreuve)

#### `Participant.java`
- **Rôle** : Membres individuels d'une équipe
- **Champs** : nom, prénom, sexe, date de naissance, statut (capitaine/suiveur)
- **Relation** : Appartient à une `Equipe` (id_equipe)

#### `Epreuve.java`
- **Rôle** : Types d'événements sportifs disponibles
- **Champs** : nom de l'épreuve, description
- **Usage** : Course à pied, VTT, Canoë, Triathlon

#### `Benevol.java`
- **Rôle** : Bénévoles inscrits pour aider à l'organisation
- **Champs** : informations personnelles, disponibilités
- **Relation** : Peut avoir un `Role` assigné (id_role nullable)

#### `Role.java`
- **Rôle** : Postes/rôles disponibles pour les bénévoles
- **Champs** : nom du rôle, quantité nécessaire, type, horaires
- **Usage** : "Accueil", "Chronométrage", "Sécurité", etc.

#### `RoleAvecAffectation.java`
- **Rôle** : Vue enrichie des rôles avec informations d'affectation
- **Usage** : Statistiques pour l'administration

---

## 🔌 **Couche DAO (Accès aux données)**

### 📂 `boldair/dao/`
Interfaces Spring Data JDBC pour l'accès aux données :

#### `DaoCompte.java`
- **Méthodes** : `findByEmail()`, `findByPseudo()`
- **Usage** : Authentification, vérification unicité email/pseudo

#### `DaoEquipe.java`
- **Méthodes** : `findByNomEquipe()`, `findByIdEpreuve()`
- **Usage** : Gestion des équipes, recherche par nom et épreuve

#### `DaoParticipant.java`
- **Méthodes** : `findByIdEquipe()`, `findByEmail()`
- **Usage** : Gestion des membres d'équipe

#### `DaoEpreuve.java`
- **Méthodes** : `findByNomEpreuve()`
- **Usage** : Récupération des types d'événements

#### `DaoBenevol.java`
- **Méthodes** : `findByEmail()`, `findByIdRole()`
- **Usage** : Gestion des bénévoles et leurs affectations

#### `DaoRole.java`
- **Méthodes** : `findAll()`, `findRolesAvecAffectation()`
- **Usage** : Gestion des postes bénévoles, statistiques

---

## ⚙️ **Couche Service (Logique métier)**

### 📂 `boldair/service/`

#### `ServiceInscription.java`
- **Rôle** : Gestion des inscriptions d'équipes
- **Méthodes principales** :
  - `inscrireEquipe()` : Inscription complète (compte + équipe + participants)
  - Validation des données, vérification unicité email
- **Usage** : Processus d'inscription public des équipes

#### `ServiceInscriptionBenevol.java`
- **Rôle** : Gestion des inscriptions de bénévoles
- **Méthodes principales** :
  - `inscrireBenevol()` : Inscription des bénévoles
  - Gestion des disponibilités et préférences
- **Usage** : Processus d'inscription public des bénévoles

#### `ServiceAdmin.java`
- **Rôle** : Fonctionnalités d'administration
- **Méthodes principales** :
  - Gestion des équipes et bénévoles
  - Affectation des rôles aux bénévoles
  - Statistiques et rapports
- **Usage** : Interface d'administration

#### `ServiceSecurity.java`
- **Rôle** : Gestion de la sécurité et authentification
- **Méthodes** : Validation des credentials, gestion des sessions
- **Usage** : Connexion admin/bénévol/utilisateur

---

## 🌐 **Couche Web (Contrôleurs)**

### 📂 `boldair/web/`

#### `WebPublic.java`
- **Rôle** : Pages publiques accessibles sans authentification
- **Endpoints** :
  - `/` : Page d'accueil
  - `/inscription-equipe` : Formulaire d'inscription équipes
  - `/inscription-benevol` : Formulaire d'inscription bénévoles
  - `/classement` : Affichage des résultats
- **Usage** : Interface publique du site

#### `WebSecurity.java`
- **Rôle** : Gestion de l'authentification
- **Endpoints** :
  - `/login` : Page de connexion
  - `/logout` : Déconnexion
- **Usage** : Connexion admin/bénévol/utilisateur

#### `WebCompte.java`
- **Rôle** : Pages privées après authentification
- **Endpoints** :
  - `/compte/admin` : Interface d'administration
  - `/compte/benevol` : Interface bénévole
  - `/compte/utilisateur` : Interface équipe
  - `/compte/admin/benevoles/export` : Export formaté des bénévoles (PDF/impression)
  - `/compte/admin/equipes/export` : Export formaté des équipes (PDF/impression)
- **Sécurité** : Accès basé sur les rôles

#### `WebInscriptionBenevol.java`
- **Rôle** : Gestion spécifique des inscriptions bénévoles
- **Usage** : Traitement des formulaires bénévoles

---

## 🎨 **Interface utilisateur (Templates)**

### 📂 `src/main/resources/templates/`

#### `public/`
**Pages accessibles à tous** :
- `accueil.html` : Page d'accueil avec présentation de l'événement
- `inscription-equipe.html` : Formulaire d'inscription des équipes
- `inscription-benevol.html` : Formulaire d'inscription des bénévoles
- `classement.html` : Résultats et classements

#### `compte/`
**Pages privées selon le rôle** :

##### `admin.html`
- **Utilisateurs** : Administrateurs uniquement
- **Fonctionnalités** :
  - Vue d'ensemble de l'événement
  - Gestion des équipes inscrites
  - Gestion des bénévoles
  - Affectation des rôles
  - Statistiques globales

##### `benevol.html`
- **Utilisateurs** : Bénévoles connectés
- **Fonctionnalités** :
  - Consultation de son affectation
  - Informations sur son poste
  - Planning personnel

##### `utilisateur.html`
- **Utilisateurs** : Équipes connectées
- **Fonctionnalités** :
  - Consultation des informations de l'équipe
  - Modification des données
  - Statut d'inscription et paiement

##### Pages d'administration détaillées :
- `admin-equipes.html` : Gestion détaillée des équipes
- `admin-benevoles.html` : Gestion détaillée des bénévoles

##### Pages d'export formatées :
- `export-benevoles.html` : Export imprimable de la liste des bénévoles
- `export-equipes.html` : Export imprimable de la liste des équipes avec participants

---

## 🗃️ **Base de données (Scripts SQL)**

### 📂 `src/main/resources/db/`

#### Scripts de structure :
- `1a-schema.sql` : Création du schéma de base
- `1b-tables.sql` : Création des tables principales
- `2-procedures.sql` : Procédures stockées

#### Scripts de données :
- `3_delete.sql` : Nettoyage des données de test
- `4-epreuve-data.sql` : Données des épreuves sportives
- `5-roles.sql` : Rôles disponibles pour les bénévoles
- `3-compte.sql` : Données de test (comptes, équipes, participants)
- `6-test-data.sql` : Données additionnelles de test

---

## 🔐 **Sécurité et Configuration**

### `boldair/security/`

#### `ConfigSecurity.java`
- **Rôle** : Configuration Spring Security
- **Règles** : Définit les accès par rôle et les pages protégées

#### `CustomAuthenticationSuccessHandler.java`
- **Rôle** : Redirection après connexion selon le rôle
- **Logique** : Admin → `/compte/admin`, Bénévol → `/compte/benevol`, etc.

---

## 🛠️ **Utilitaires**

### `boldair/util/`
- `Alert.java` : Gestion des messages d'alerte
- `Paging.java` : Gestion de la pagination
- `Photos.java` : Gestion des images

---

## 🔄 **Flux d'utilisation typiques**

### 1. **Inscription d'une équipe** :
`WebPublic` → `ServiceInscription` → `DaoCompte/DaoEquipe/DaoParticipant`

### 2. **Inscription d'un bénévole** :
`WebPublic` → `ServiceInscriptionBenevol` → `DaoCompte/DaoBenevol`

### 3. **Connexion utilisateur** :
`WebSecurity` → `ServiceSecurity` → `DaoCompte` → Redirection selon rôle

### 4. **Administration** :
`WebCompte` → `ServiceAdmin` → Divers DAO → Pages admin

---

## 📱 **Points d'entrée par type d'utilisateur**

| Type d'utilisateur | Point d'entrée | Pages accessibles |
|-------------------|-----------------|-------------------|
| **Visiteur** | `/` | Accueil, inscriptions, classement |
| **Équipe** | `/login` → `/compte/utilisateur` | Gestion de l'équipe |
| **Bénévole** | `/login` → `/compte/benevol` | Consultation affectation |
| **Admin** | `/login` → `/compte/admin` | Gestion complète |

Cette architecture suit le pattern MVC avec une séparation claire entre les couches de données, logique métier et présentation, permettant une maintenance et évolution facilites du code.