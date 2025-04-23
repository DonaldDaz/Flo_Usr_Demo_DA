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

-- Additional example rows

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Davide', 'Neri', 'davide.neri@example.com', 'Via Garibaldi 4, Napoli')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Elena', 'Greco', 'elena.greco@example.com', 'Viale Europa 5, Firenze')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Fabio', 'Russo', 'fabio.russo@example.com', 'Piazza San Marco 6, Venezia')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Giorgia', 'Esposito', 'giorgia.esposito@example.com', 'Via Cavour 7, Bari')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Luca', 'Fontana', 'luca.fontana@example.com', 'Corso Venezia 8, Verona')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Maria', 'Conti', 'maria.conti@example.com', 'Via del Corso 9, Roma')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Paolo', 'Marini', 'paolo.marini@example.com', 'Piazza Maggiore 10, Bologna')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Laura', 'Gallo', 'laura.gallo@example.com', 'Via Toledo 11, Napoli')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Marco', 'De Luca', 'marco.deluca@example.com', 'Piazza Castello 12, Milano')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, address) VALUES
    ('Silvia', 'Puglisi', 'silvia.puglisi@example.com', 'Via Etnea 13, Catania')
    ON CONFLICT (email) DO NOTHING;
