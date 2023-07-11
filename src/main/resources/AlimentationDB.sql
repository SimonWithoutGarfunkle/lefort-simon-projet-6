--Suppression des données existantes
DELETE FROM transaction;
DELETE FROM utilisateur;
ALTER TABLE utilisateur AUTO_INCREMENT = 1;
ALTER TABLE transaction AUTO_INCREMENT = 1;

-- Insertion des comptes
INSERT INTO comptepmb (montant)
VALUES
    (1000.0),
    (2000.0),
    (3000.0),
    (4000.0),
    (5000.0);

-- Insertion des utilisateurs
INSERT INTO utilisateur (email, mot_de_passe, nom, prenom)
VALUES
    ('utilisateur1@example.com', 'motdepasse1', 'Nom1', 'Prénom1'),
    ('utilisateur2@example.com', 'motdepasse2', 'Nom2', 'Prénom2'),
    ('utilisateur3@example.com', 'motdepasse3', 'Nom3', 'Prénom3'),
    ('utilisateur4@example.com', 'motdepasse4', 'Nom4', 'Prénom4'),
    ('utilisateur5@example.com', 'motdepasse5', 'Nom5', 'Prénom5');

-- Insertion des transactions
INSERT INTO transaction (emetteur_id, destinataire_id, somme, commission, horodatage, status, etat_facturation)
VALUES
    (1, 2, 100.0, 5.0, CURRENT_TIMESTAMP, 'VALIDE', 'A_EDITER'),
    (1, 3, 50.0, 2.5, CURRENT_TIMESTAMP, 'EN_COURS', 'EN_COURS'),
    (2, 4, 75.0, 3.75, CURRENT_TIMESTAMP, 'VALIDE', 'GENEREE'),
    (3, 5, 200.0, 10.0, CURRENT_TIMESTAMP, 'EN_COURS', 'COMPTABILISEE'),
    (4, 5, 150.0, 7.5, CURRENT_TIMESTAMP, 'VALIDE', 'COMPTABILISEE');
