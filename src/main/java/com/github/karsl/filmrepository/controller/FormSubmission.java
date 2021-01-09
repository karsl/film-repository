package com.github.karsl.filmrepository.controller;

import com.github.karsl.filmrepository.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO like object to handle form submissions.
 */
@Data
public class FormSubmission {

    Long id;
    String title;
    String description;
    List<Long> languages = new ArrayList<>();
    Long genre;
    Integer year;
    Long media;
    List<CreditTuple> credits = new ArrayList<>();

    public Film mapToFilm() {
        Film film = new Film();

        film.setId(id);

        film.setTitle(title);
        film.setDescription(description);

        film.setLanguages(languages.stream().map(id -> {
            Language language = new Language();
            language.setId(id);
            return language;
        }).collect(Collectors.toSet()));

        FilmGenre filmGenre = new FilmGenre();
        filmGenre.setId(genre);
        film.setGenre(filmGenre);

        film.setYear(year);

        MediaType mediaType = new MediaType();
        mediaType.setId(media);
        film.setMedia(mediaType);

        film.setCredits(credits.stream().map(c -> {
            Actor actor = new Actor();
            actor.setFullName(c.creditName);

            Credit credit = new Credit();
            credit.setActor(actor);
            credit.setRole(c.creditRole);
            credit.setFilm(film);

            return credit;
        }).collect(Collectors.toSet()));

        return film;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditTuple {
        String creditName;
        String creditRole;
    }

}
