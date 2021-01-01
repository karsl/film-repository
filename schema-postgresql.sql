CREATE SCHEMA filmrepository;

CREATE TABLE film_genre
(
    id   SERIAL PRIMARY KEY,
    name text NOT NULL UNIQUE
);

CREATE TABLE media_type
(
    id   SERIAL PRIMARY KEY,
    name text NOT NULL UNIQUE
);

CREATE TABLE language
(
    id   SERIAL PRIMARY KEY,
    name text NOT NULL UNIQUE
);

CREATE TABLE film
(
    id          SERIAL PRIMARY KEY,
    title       text     NOT NULL,
    description text,
    genre       int      NOT NULL REFERENCES film_genre (id),
    year        smallint NOT NULL DEFAULT date_part('year', CURRENT_DATE) CHECK ( year > 0 ),
    media       int      NOT NULL REFERENCES media_type (id)
);

CREATE TABLE available_language
(
    film_id     integer NOT NULL REFERENCES Film (id),
    language_id integer NOT NULL REFERENCES language (id),

    UNIQUE (film_id, language_id)
);

CREATE TABLE actor
(
    id        SERIAL PRIMARY KEY,
    full_name text NOT NULL
);

CREATE TABLE credit
(
    film_id  integer NOT NULL REFERENCES Film (id),
    actor_id integer NOT NULL REFERENCES Actor (id),
    role     text    NOT NULL,

    UNIQUE (film_id, actor_id, role)
);

