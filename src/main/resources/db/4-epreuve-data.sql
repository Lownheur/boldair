-------
-- Données d'initialisation pour la table Epreuve
-------
SET search_path TO boldair, public;

-- Insert default Epreuve record
INSERT INTO epreuve (id_epreuve, nom_bol_dair) VALUES
(1, 'Mini Bol d''Air'),
(2, 'Bol d''Air'),
(3, 'Bol d''Air Découverte'),
(4, 'Triathlon');

-- Reset the sequence for Id_Epreuve to start with 5 for the next insertion
SELECT setval('epreuve_id_epreuve_seq', 4, true);
