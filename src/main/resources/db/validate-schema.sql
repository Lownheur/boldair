-- Test script to validate schema and table structure
-- This script should be run after 1a-schema.sql, 1b-tables.sql, and 4-epreuve-data.sql

-- Set the search path to use our schema
SET search_path TO boldair;

-- Verify tables exist in boldair schema
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'boldair'
ORDER BY table_name;

-- Check that required tables exist
SELECT 'compte exists' as status WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'boldair' AND table_name = 'compte');
SELECT 'Equipe exists' as status WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'boldair' AND table_name = 'Equipe');
SELECT 'Participant exists' as status WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'boldair' AND table_name = 'Participant');
SELECT 'Benevol exists' as status WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'boldair' AND table_name = 'Benevol');
SELECT 'Epreuve exists' as status WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'boldair' AND table_name = 'Epreuve');

-- Check that Epreuve data exists
SELECT 'Epreuve data exists' as status, Id_Epreuve, nom_bol_dair FROM Epreuve WHERE Id_Epreuve = 1;

-- Test basic insertions to validate the schema works
-- Insert a test compte
INSERT INTO compte (username, password_hash, email, role)
VALUES ('test@test.com', 'hashedpassword', 'test@test.com', 'utilisateur');

-- Get the inserted compte id
SELECT Id_Compte FROM compte WHERE username = 'test@test.com';

-- Test cleanup
DELETE FROM compte WHERE username = 'test@test.com';

SELECT 'Schema validation completed successfully' as status;
