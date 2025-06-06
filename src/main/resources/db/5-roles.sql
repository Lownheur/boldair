-- Set search path to boldair schema
SET search_path TO boldair, public;

INSERT INTO roles (nomrole, quantité, type_benevole, horaire, bénévoles) VALUES
('Parking voiture / Parking vélo', '2', 'M', '7h – 9h', NULL),
('Remise des dossards', '4', 'M', '7h - 9h', NULL),
('Signaleur', '37 postes', 'M et E', '8h30 – 13h30', NULL),
('Ravitaillement', '3 postes 2 pers (6)', 'M et E', '9h – 13h', NULL),
('Sécurité sur eau', '6', 'M', '9h-10h30', NULL),
('Chronométrage même poste pour les 2 bols', '3 chronos (course à pieds, canoë, arrivée Vtt) 2 pers mini (1 manuel + 1 badgage)', 'M et E', '9h30 / 10h15 / 13h30', NULL),
('Moto (fermeture)', '2', 'M et/ou E', '9h - 13h30', NULL),
('Buvette', '5 (prévoir relais)', 'M et/ou E', '7h – 15h', NULL),
('Repas', '3 à partir de 11h', 'E', '12h – 14h', NULL),
('Récupérer les dossards et puces', '1', 'M', '12h – 13h30', NULL),
('Photographe', '1 à 2', 'E ou M', '7h - 14h', NULL);