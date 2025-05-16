-------
-- Ajout d'extension à PostgreSQL
-------
DROP EXTENSION IF EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


-------
-- compte
-------

INSERT INTO compte (id_compte, pseudo, empreinte_mdp, email, role_admin, role_benevol) VALUES
( 1, 'admin', crypt( 'admin', gen_salt('bf')), 'admin@mail.com', TRUE, FALSE ),
( 2, 'max', crypt('max', gen_salt('bf')), 'max@mail.com', FALSE, FALSE ),
( 3, 'mika', crypt('mika', gen_salt('bf')), 'mika@mail.com', FALSE, TRUE ),
( 4, 'tom', crypt('tom', gen_salt('bf')), 'tom@mail.com', FALSE, FALSE ),
( 5, 'eva', crypt('eva', gen_salt('bf')), 'eva@mail.com', FALSE, FALSE );

-- Reset the sequence for id_compte to start with 10 for the next insertion
SELECT setval('compte_id_compte_seq', 10, false);

-------
-- benevol
-------

-- Insert into Benevol table
INSERT INTO Benevol (interne___externe, nom, email, téléphone, prénom, statut) VALUES
(TRUE, 'Durand', 'mika@mail.com', '0612345678', 'Mikael', 'Actif');

-- Link the benevol to the compte (mika)
INSERT INTO compte_benevol (Id_Benevol, id_compte)
SELECT b.Id_Benevol, c.id_compte 
FROM Benevol b, compte c 
WHERE b.email = c.email AND c.pseudo = 'mika';
