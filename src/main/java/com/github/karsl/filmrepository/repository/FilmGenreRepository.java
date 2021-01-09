package com.github.karsl.filmrepository.repository;

import com.github.karsl.filmrepository.model.FilmGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmGenreRepository extends JpaRepository<FilmGenre, Long> {

}
