-- scripts/schema.sql
-- Create the users table in the flo_usr_demo database

DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id         SERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name  VARCHAR(100) NOT NULL,
                       email      VARCHAR(150) NOT NULL UNIQUE,
                       address    VARCHAR(255)
);

