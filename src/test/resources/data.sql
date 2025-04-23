-- scripts/data.sql
-- Populate the users table with some example rows

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Alice', 'Rossi', 'alice.rossi@example.com', 'Via Roma 1, Roma')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Bruno', 'Bianchi', 'bruno.bianchi@example.com', 'Piazza Duomo 2, Milano')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Carla', 'Verdi', 'carla.verdi@example.com', 'Corso Italia 3, Torino')
    ON CONFLICT (email) DO NOTHING;

