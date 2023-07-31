--Suppression des données existantes
DELETE FROM transaction;
DELETE FROM contact;
DELETE FROM utilisateur;
DELETE FROM comptepmb;
DELETE FROM rib;
DELETE FROM contact;
ALTER TABLE contact AUTO_INCREMENT = 1;
ALTER TABLE utilisateur AUTO_INCREMENT = 1;
ALTER TABLE rib AUTO_INCREMENT = 1;
ALTER TABLE comptepmb AUTO_INCREMENT = 1;
ALTER TABLE transaction AUTO_INCREMENT = 1;

-- Insertion des comptes
INSERT INTO comptepmb (montant)
VALUES
    (1000.0),
    (2000.0),
    (3000.0),
    (4000.0),
    (5000.0),
    (50000.0);
    
-- Insertion des RIB
INSERT INTO rib (iban)
VALUES
    ('IBAN1'),
    ('IBAN2'),
    ('IBAN3'),
    ('IBAN4'),
    ('IBAN5'),
    ('IBAN6');

-- Insertion des utilisateurs
INSERT INTO utilisateur (email, mot_de_passe, nom, prenom, rib_id, comptepmb_id, role)
VALUES
    ('utilisateur1@example.com', 'motdepasse1', 'Nom1', 'Prenom1', 1, 1, 'USER'),
    ('utilisateur2@example.com', 'motdepasse2', 'Nom2', 'Prenom2', 2, 2, 'USER'),
    ('utilisateur3@example.com', 'motdepasse3', 'Nom3', 'Prenom3', 3, 3, 'USER'),
    ('utilisateur4@example.com', 'motdepasse4', 'Nom4', 'Prenom4', 4, 4, 'USER'),
    ('utilisateur5@example.com', 'motdepasse5', 'Nom5', 'Prenom5', 5, 5, 'USER'),
    ('simonlefort@hotmail.fr', 'aaa', 'Lefort', 'Simon', 6, 6, 'USER');

-- Insertion des transactions
INSERT INTO transaction (emetteur_id, destinataire_id, somme, commission, horodatage, status, etat_facturation)
VALUES
    (1, 6, 100.0, 5.0, CURRENT_TIMESTAMP, 'VALIDE', 'A_EDITER'),
    (1, 3, 50.0, 2.5, CURRENT_TIMESTAMP, 'EN_COURS', 'EN_COURS'),
    (2, 4, 75.0, 3.75, CURRENT_TIMESTAMP, 'VALIDE', 'GENEREE'),
    (3, 5, 200.0, 10.0, CURRENT_TIMESTAMP, 'EN_COURS', 'COMPTABILISEE'),
    (4, 5, 150.0, 7.5, CURRENT_TIMESTAMP, 'VALIDE', 'COMPTABILISEE'),
    (6, 1, 50.0, 2.5, CURRENT_TIMESTAMP, 'EN_COURS', 'EN_COURS'),
    (6, 3, 75.0, 3.75, CURRENT_TIMESTAMP, 'VALIDE', 'GENEREE');
    
-- Insertion des contactq
INSERT INTO contact (commentaire, email, nom, prenom, surnom, utilisateur_id )
VALUES
    ('coucou1', 'utilisateur1@example.com', 'Nom1', 'Prenom1', 'surnom1', 6),
    ('coucou2', 'utilisateur2@example.com', 'Nom2', 'Prenom2', 'surnom2', 6),
    ('coucou3', 'utilisateur3@example.com', 'Nom3', 'Prenom3', 'surnom3', 6),
    ('coucou4', 'utilisateur4@example.com', 'Nom4', 'Prenom4', 'surnom4', 6),
    ('coucou5', 'utilisateur5@example.com', 'Nom5', 'Prenom5', 'surnom5', 6),
    ('coucou6', 'utilisateur6@example.com', 'Nom1', 'Prenom1', 'surnom1', 6),
    ('coucou7', 'utilisateur7@example.com', 'Nom2', 'Prenom2', 'surnom2', 6),
    ('coucou8', 'utilisateur8@example.com', 'Nom3', 'Prenom3', 'surnom3', 2),
    ('coucou9', 'utilisateur9@example.com', 'Nom4', 'Prenom4', 'surnom4', 1);
