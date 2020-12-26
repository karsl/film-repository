INSERT INTO film (id, title, description, genre, media)
VALUES (1, 'The Shawshank Redemption',
        'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.',
        'Drama', 'Stream'),
       (2, 'The Godfather',
        'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.',
        'Crime', 'Download');

INSERT INTO Actor (id, fullname)
VALUES (1, 'Tim Robbins'),
       (2, 'Morgan Freeman'),
       (3, 'Bob Gunton'),
       (4, 'William Sadler'),
       (5, 'Marlon Brando'),
       (6, 'Al Pacino'),
       (7, 'James Caan'),
       (8, 'Richard S. Castellano');

INSERT INTO Language (Name)
VALUES ('English'),
       ('Italian'),
       ('Latin');

INSERT INTO Credit (filmid, actorid, role)
VALUES (1, 1, 'Andy Dufresne'),
       (1, 2, 'Elis Boyd "Red" Redding'),
       (1, 3, 'Warden Norton'),
       (1, 4, 'Heywood'),
       (2, 5, 'Don Vito Corleone'),
       (2, 6, 'Micheal Corleone'),
       (2, 7, 'Sonny Corleone'),
       (2, 8, 'Clemenza');

INSERT INTO AvailableLanguage (filmid, language)
VALUES (1, 'English'),
       (2, 'English'),
       (2, 'Italian'),
       (2, 'Latin');
