-------
-- Ajout d'extension à PostgreSQL
-------
DROP EXTENSION IF EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


-------
-- compte
-------

INSERT INTO compte (id_compte, pseudo, empreinte_mdp, email, role_admin ) VALUES
( 1, 'admin', crypt( 'admin', gen_salt('bf')), 'admin@mail.com', TRUE ),
( 2, 'max', crypt('max', gen_salt('bf')), 'max@mail.com', FALSE ),
( 3, 'mika', crypt('mika', gen_salt('bf')), 'mika@mail.com', FALSE ),
( 4, 'tom', crypt('tom', gen_salt('bf')), 'tom@mail.com', FALSE ),
( 5, 'eva', crypt('eva', gen_salt('bf')), 'eva@mail.com', FALSE );

ALTER TABLE compte      ALTER COLUMN id_compte     RESTART WITH 10;
