-- Schéma

DROP SCHEMA IF EXISTS boldair CASCADE;
CREATE SCHEMA boldair;

-- Enable pgcrypto extension if not already enabled
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Set default search path for subsequent operations
SET search_path TO boldair, public;
