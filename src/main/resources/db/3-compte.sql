-------
-- Ajout d'extension à PostgreSQL
-------
SET search_path TO boldair, public;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-------
-- 30 comptes : 1 admin + 10 bénévoles + 10 équipes (20 participants) + 9 autres
-------

INSERT INTO compte (id_compte, pseudo, empreinte_mdp, email, role_admin, role_benevol) VALUES
-- Admin
( 1, 'admin', crypt( 'admin', gen_salt('bf')), 'admin@boldair.com', TRUE, FALSE ),

-- 10 comptes bénévoles
( 2, 'marie.dupont', crypt('marie123', gen_salt('bf')), 'marie.dupont@email.com', FALSE, TRUE ),
( 3, 'jean.martin', crypt('jean123', gen_salt('bf')), 'jean.martin@email.com', FALSE, TRUE ),
( 4, 'pierre.bernard', crypt('pierre123', gen_salt('bf')), 'pierre.bernard@email.com', FALSE, TRUE ),
( 5, 'sophie.robert', crypt('sophie123', gen_salt('bf')), 'sophie.robert@email.com', FALSE, TRUE ),
( 6, 'luc.michel', crypt('luc123', gen_salt('bf')), 'luc.michel@email.com', FALSE, TRUE ),
( 7, 'claire.moreau', crypt('claire123', gen_salt('bf')), 'claire.moreau@email.com', FALSE, TRUE ),
( 8, 'thomas.petit', crypt('thomas123', gen_salt('bf')), 'thomas.petit@email.com', FALSE, TRUE ),
( 9, 'julie.david', crypt('julie123', gen_salt('bf')), 'julie.david@email.com', FALSE, TRUE ),
( 10, 'nicolas.garcia', crypt('nicolas123', gen_salt('bf')), 'nicolas.garcia@email.com', FALSE, TRUE ),
( 11, 'emma.rodriguez', crypt('emma123', gen_salt('bf')), 'emma.rodriguez@email.com', FALSE, TRUE ),

-- 10 comptes d'équipes (pour 20 participants)
( 12, 'equipe.coureurs', crypt('coureurs123', gen_salt('bf')), 'captain.coureurs@email.com', FALSE, FALSE ),
( 13, 'equipe.vttwarriors', crypt('vtt123', gen_salt('bf')), 'captain.vttwarriors@email.com', FALSE, FALSE ),
( 14, 'equipe.aquateam', crypt('aqua123', gen_salt('bf')), 'captain.aquateam@email.com', FALSE, FALSE ),
( 15, 'equipe.ironsquad', crypt('iron123', gen_salt('bf')), 'captain.ironsquad@email.com', FALSE, FALSE ),
( 16, 'equipe.speedrunners', crypt('speed123', gen_salt('bf')), 'captain.speedrunners@email.com', FALSE, FALSE ),
( 17, 'equipe.mountainbikers', crypt('mountain123', gen_salt('bf')), 'captain.mountainbikers@email.com', FALSE, FALSE ),
( 18, 'equipe.waterwolves', crypt('water123', gen_salt('bf')), 'captain.waterwolves@email.com', FALSE, FALSE ),
( 19, 'equipe.triforce', crypt('tri123', gen_salt('bf')), 'captain.triforce@email.com', FALSE, FALSE ),
( 20, 'equipe.flashteam', crypt('flash123', gen_salt('bf')), 'captain.flashteam@email.com', FALSE, FALSE ),
( 21, 'equipe.paddlepower', crypt('paddle123', gen_salt('bf')), 'captain.paddlepower@email.com', FALSE, FALSE ),

-- 9 autres comptes
( 22, 'alex.durand', crypt('alex123', gen_salt('bf')), 'alex.durand@email.com', FALSE, FALSE ),
( 23, 'camille.blanc', crypt('camille123', gen_salt('bf')), 'camille.blanc@email.com', FALSE, FALSE ),
( 24, 'lucas.guerin', crypt('lucas123', gen_salt('bf')), 'lucas.guerin@email.com', FALSE, FALSE ),
( 25, 'laura.joly', crypt('laura123', gen_salt('bf')), 'laura.joly@email.com', FALSE, FALSE ),
( 26, 'mathieu.riviere', crypt('mathieu123', gen_salt('bf')), 'mathieu.riviere@email.com', FALSE, FALSE ),
( 27, 'celine.lopez', crypt('celine123', gen_salt('bf')), 'celine.lopez@email.com', FALSE, FALSE ),
( 28, 'anthony.roy', crypt('anthony123', gen_salt('bf')), 'anthony.roy@email.com', FALSE, FALSE ),
( 29, 'marion.clement', crypt('marion123', gen_salt('bf')), 'marion.clement@email.com', FALSE, FALSE ),
( 30, 'vincent.gauthier', crypt('vincent123', gen_salt('bf')), 'vincent.gauthier@email.com', FALSE, FALSE );

-- Reset the sequence for id_compte to start with 40 for the next insertion
SELECT setval('compte_id_compte_seq', 40, false);

-------
-- 10 bénévoles
-------

INSERT INTO Benevol (interne___externe, nom, email, téléphone, prénom, statut, heure_debut_dispo, heure_fin_dispo, commentaire, permis, interne, id_compte, id_role) VALUES
(TRUE, 'Dupont', 'marie.dupont@email.com', '0123456789', 'Marie', 'Actif', '07:00', '15:00', 'Experience en organisation', TRUE, TRUE, 2, NULL),
(FALSE, 'Martin', 'jean.martin@email.com', '0123456790', 'Jean', 'Actif', '08:00', '14:00', 'Premier evenement', FALSE, FALSE, 3, NULL),
(TRUE, 'Bernard', 'pierre.bernard@email.com', '0123456791', 'Pierre', 'Actif', '07:30', '13:30', 'Disponible toute la journee', TRUE, TRUE, 4, NULL),
(FALSE, 'Robert', 'sophie.robert@email.com', '0123456792', 'Sophie', 'Actif', '09:00', '16:00', 'Bonne communication', FALSE, FALSE, 5, NULL),
(TRUE, 'Michel', 'luc.michel@email.com', '0123456793', 'Luc', 'Actif', '06:00', '12:00', 'Lever tot, pas de probleme', TRUE, TRUE, 6, NULL),
(FALSE, 'Moreau', 'claire.moreau@email.com', '0123456794', 'Claire', 'Actif', '10:00', '18:00', 'Prefere apres-midi', FALSE, FALSE, 7, NULL),
(TRUE, 'Petit', 'thomas.petit@email.com', '0123456795', 'Thomas', 'Actif', '08:30', '14:30', 'Etudiant motive', TRUE, TRUE, 8, NULL),
(FALSE, 'David', 'julie.david@email.com', '0123456796', 'Julie', 'Actif', '07:00', '13:00', 'Experience medicale', FALSE, FALSE, 9, NULL),
(TRUE, 'Garcia', 'nicolas.garcia@email.com', '0123456797', 'Nicolas', 'Actif', '09:30', '15:30', 'Passionne de sport', TRUE, TRUE, 10, NULL),
(FALSE, 'Rodriguez', 'emma.rodriguez@email.com', '0123456798', 'Emma', 'Actif', '08:00', '16:00', 'Tres flexible', FALSE, FALSE, 11, NULL);

-------
-- 10 équipes
-------

INSERT INTO equipe (nom_equipe, nom_bol_dair, num_classement, catégorie, temps_retenu, paid, numero_dossard, ticket_repas, id_epreuve) VALUES
('Les Coureurs', 'Course a pied', '1', 'Senior', '45:30', TRUE, '001', 'Oui', 1),
('VTT Warriors', 'VTT', '2', 'Senior', '1:20:45', TRUE, '002', 'Oui', 2),
('Aqua Team', 'Canoe', '3', 'Junior', '35:12', TRUE, '003', 'Non', 3),
('Iron Squad', 'Triathlon', '4', 'Senior', '2:15:30', FALSE, '004', 'Oui', 4),
('Speed Runners', 'Course a pied', '5', 'Veteran', '48:20', TRUE, '005', 'Non', 1),
('Mountain Bikers', 'VTT', '6', 'Senior', '1:25:10', TRUE, '006', 'Oui', 2),
('Water Wolves', 'Canoe', '7', 'Senior', '38:45', FALSE, '007', 'Non', 3),
('Tri Force', 'Triathlon', '8', 'Junior', '2:30:15', TRUE, '008', 'Oui', 4),
('Flash Team', 'Course a pied', '9', 'Senior', '42:55', TRUE, '009', 'Non', 1),
('Paddle Power', 'Canoe', '10', 'Veteran', '40:30', FALSE, '010', 'Oui', 3);

-------
-- 20 participants (2 par équipe)
-------

INSERT INTO participant (nom, prénom, status, nom_equipe, sexe, bol_d_air, date_de_naissance, email, certificat_médical, num_puce, id_equipe) VALUES
('Dubois', 'Antoine', 'Confirme', 'Les Coureurs', 'M', 'Course a pied', '1990-05-15', 'antoine.dubois@email.com', 'Oui', '001A', 1),
('Leroy', 'Camille', 'Confirme', 'Les Coureurs', 'F', 'Course a pied', '1992-08-22', 'camille.leroy@email.com', 'Oui', '001B', 1),
('Roux', 'Maxime', 'Expert', 'VTT Warriors', 'M', 'VTT', '1988-12-10', 'maxime.roux@email.com', 'Oui', '002A', 2),
('Fournier', 'Lisa', 'Expert', 'VTT Warriors', 'F', 'VTT', '1991-03-18', 'lisa.fournier@email.com', 'Oui', '002B', 2),
('Girard', 'Hugo', 'Debutant', 'Aqua Team', 'M', 'Canoe', '2000-07-08', 'hugo.girard@email.com', 'Oui', '003A', 3),
('Bonnet', 'Lea', 'Debutant', 'Aqua Team', 'F', 'Canoe', '1999-11-25', 'lea.bonnet@email.com', 'Oui', '003B', 3),
('Dupuis', 'Julien', 'Expert', 'Iron Squad', 'M', 'Triathlon', '1987-01-30', 'julien.dupuis@email.com', 'Non', '004A', 4),
('Lambert', 'Sarah', 'Expert', 'Iron Squad', 'F', 'Triathlon', '1989-09-14', 'sarah.lambert@email.com', 'Oui', '004B', 4),
('Fontaine', 'Paul', 'Confirme', 'Speed Runners', 'M', 'Course a pied', '1975-06-20', 'paul.fontaine@email.com', 'Oui', '005A', 5),
('Rousseau', 'Nathalie', 'Confirme', 'Speed Runners', 'F', 'Course a pied', '1978-02-12', 'nathalie.rousseau@email.com', 'Oui', '005B', 5),
('Morel', 'Kevin', 'Expert', 'Mountain Bikers', 'M', 'VTT', '1993-04-05', 'kevin.morel@email.com', 'Oui', '006A', 6),
('Simon', 'Marine', 'Expert', 'Mountain Bikers', 'F', 'VTT', '1994-10-28', 'marine.simon@email.com', 'Oui', '006B', 6),
('Laurent', 'Romain', 'Confirme', 'Water Wolves', 'M', 'Canoe', '1990-12-03', 'romain.laurent@email.com', 'Non', '007A', 7),
('Lefebvre', 'Amelie', 'Confirme', 'Water Wolves', 'F', 'Canoe', '1992-08-17', 'amelie.lefebvre@email.com', 'Oui', '007B', 7),
('Michel', 'Alexandre', 'Debutant', 'Tri Force', 'M', 'Triathlon', '2001-03-22', 'alexandre.michel@email.com', 'Oui', '008A', 8),
('Garcia', 'Manon', 'Debutant', 'Tri Force', 'F', 'Triathlon', '2000-09-11', 'manon.garcia@email.com', 'Oui', '008B', 8),
('Thomas', 'Fabien', 'Expert', 'Flash Team', 'M', 'Course a pied', '1985-11-08', 'fabien.thomas@email.com', 'Oui', '009A', 9),
('Masson', 'Emilie', 'Expert', 'Flash Team', 'F', 'Course a pied', '1986-07-24', 'emilie.masson@email.com', 'Oui', '009B', 9),
('Faure', 'Sebastien', 'Confirme', 'Paddle Power', 'M', 'Canoe', '1972-05-16', 'sebastien.faure@email.com', 'Oui', '010A', 10),
('Andre', 'Valerie', 'Confirme', 'Paddle Power', 'F', 'Canoe', '1974-01-09', 'valerie.andre@email.com', 'Oui', '010B', 10);

-------
-- Admin record
-------

INSERT INTO admin DEFAULT VALUES;
