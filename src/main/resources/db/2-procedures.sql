-- Supprime tous les triggers du schema

DO
$code$
   DECLARE
      r RECORD;
   BEGIN
      FOR r IN
         SELECT 'DROP TRIGGER ' || trigger_name || ' ON ' || event_object_table AS sql
			FROM information_schema.triggers t
            WHERE trigger_schema = CURRENT_SCHEMA
            GROUP BY event_object_table, trigger_name
            LOOP
               EXECUTE r.sql;
            END LOOP;
   END;
$code$;


-- Supprime toutes les fonctions du schema

DO
$code$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT 'DROP FUNCTION IF EXISTS ' || ns.nspname || '.' || proname || '(' || oidvectortypes(proargtypes) || ')' AS sql
        FROM pg_proc
        INNER JOIN pg_namespace ns ON (pg_proc.pronamespace = ns.oid)
        LEFT JOIN pg_depend d ON d.objid = pg_proc.oid AND d.deptype = 'e'
        LEFT JOIN pg_extension e ON e.oid = d.refobjid
        WHERE ns.nspname = current_schema
        AND proname NOT IN (
            'digest',
            'hmac',
            'crypt',
            'gen_salt',
            'encrypt',
            'decrypt',
            'encrypt_iv',
            'decrypt_iv',
            'gen_random_bytes',
            'pgp_sym_encrypt',
            'pgp_sym_decrypt',
            'pgp_pub_encrypt',
            'pgp_pub_decrypt',
            'dearmor',
            'armor',
            'pgp_key_id',
            'gen_random_uuid'
        )
        AND (e.extname IS NULL OR e.extname <> 'pgcrypto')
        AND ns.nspname NOT IN ('pg_catalog', 'information_schema')
    LOOP
        EXECUTE r.sql;
    END LOOP;
END;
$code$;

-- Fonctions

-- Triggers
