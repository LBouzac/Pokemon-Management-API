INSERT INTO dresseur (nom, region) VALUES ('Sacha', 'Kanto');
INSERT INTO dresseur (nom, region) VALUES ('Ondine', 'Kanto');
INSERT INTO dresseur (nom, region) VALUES ('Novice', 'Johto');

INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Dracaufeu', 'Feu', 55, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Tortank', 'Eau', 40, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Pikachu', 'Électrik', 30, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Staross', 'Eau', 35, (SELECT id FROM dresseur WHERE nom = 'Ondine'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Psykokwak', 'Eau', 20, (SELECT id FROM dresseur WHERE nom = 'Ondine'));
