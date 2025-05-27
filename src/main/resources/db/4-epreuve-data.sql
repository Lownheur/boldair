-------
-- Données d'initialisation pour la table Epreuve
-------
SET search_path TO boldair, public;

-- Insert default Epreuve record
INSERT INTO Epreuve (Id_Epreuve, nom_bol_dair) VALUES
(1, 'Mini Bol d''Air'),
(2, 'Bol d''Air'),
(3, 'Bol d''Air Découverte');

-- Reset the sequence for Id_Epreuve to start with 2 for the next insertion
SELECT setval('epreuve_id_epreuve_seq', 1, true);
