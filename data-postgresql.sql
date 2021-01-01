INSERT INTO film_genre (id, name)
VALUES (1, 'Comedy'),
       (2, 'Action'),
       (3, 'Drama'),
       (4, 'Crime');

INSERT INTO media_type (id, name)
VALUES (1, 'Stream'),
       (2, 'Download');

INSERT INTO language (id, name)
VALUES (1, 'English'),
       (2, 'Italian'),
       (3, 'Latin');

INSERT INTO film (id, title, description, genre, year, media)
VALUES (1, 'The Shawshank Redemption',
        'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.',
        3, 1994, 1),
       (2, 'The Godfather',
        'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.',
        4, 1972, 2);

INSERT INTO actor (id, full_name)
VALUES (1, 'Tim Robbins'),
       (2, 'Morgan Freeman'),
       (3, 'Bob Gunton'),
       (4, 'William Sadler'),
       (5, 'Marlon Brando'),
       (6, 'Al Pacino'),
       (7, 'James Caan'),
       (8, 'Richard S. Castellano');

INSERT INTO credit (film_id, actor_id, role)
VALUES (1, 1, 'Andy Dufresne'),
       (1, 2, 'Elis Boyd "Red" Redding'),
       (1, 3, 'Warden Norton'),
       (1, 4, 'Heywood'),
       (2, 5, 'Don Vito Corleone'),
       (2, 6, 'Micheal Corleone'),
       (2, 7, 'Sonny Corleone'),
       (2, 8, 'Clemenza');

INSERT INTO available_language (film_id, language_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (2, 3);

