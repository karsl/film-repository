package com.github.karsl.filmrepository.repository;

import com.github.karsl.filmrepository.model.Film;
import com.github.karsl.filmrepository.model.FilmGenre;
import com.github.karsl.filmrepository.model.Language;
import com.github.karsl.filmrepository.model.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("FROM Language ORDER BY name")
    List<Language> getAllLanguages();

    @Query("FROM FilmGenre ORDER BY name")
    List<FilmGenre> getAllGenres();

    @Query("FROM MediaType ORDER BY name")
    List<MediaType> getAllMediaTypes();

}
