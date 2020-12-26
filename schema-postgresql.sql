CREATE SCHEMA filmrepository;

CREATE TYPE FilmGenre AS ENUM ('Comedy', 'Action', 'Drama', 'Crime');

CREATE TYPE MediaType AS ENUM ('Stream', 'Download');

CREATE TABLE Film
(
    ID          SERIAL PRIMARY KEY,
    Title       text      NOT NULL,
    Description text,
    Genre       FilmGenre NOT NULL,
    Year        smallint  NOT NULL DEFAULT date_part('year', CURRENT_DATE) CHECK ( Year > 0 ),
    Media       MediaType NOT NULL
);

CREATE TABLE AvailableLanguage
(
    FilmID   integer NOT NULL REFERENCES Film (ID),
    Language text    NOT NULL,

    UNIQUE (FilmID, Language)
);

CREATE TABLE Actor
(
    ID       SERIAL PRIMARY KEY,
    FullName text NOT NULL
);

CREATE TABLE Credit
(
    FilmID  integer NOT NULL REFERENCES Film (ID),
    ActorID integer NOT NULL REFERENCES Actor (ID),
    Role    text    NOT NULL,

    UNIQUE (FilmID, ActorID, Role)
)
