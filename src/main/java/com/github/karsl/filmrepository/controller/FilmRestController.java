package com.github.karsl.filmrepository.controller;

import com.github.karsl.filmrepository.model.Film;
import com.github.karsl.filmrepository.service.GeneralService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * I was not willing to employ REST service for this service, however deletion of a film matches
 * with REST 'pattern' so deletion functionality is provided via REST controller. Also, maybe in the
 * future I could add a total REST service.
 */
@RestController
@RequestMapping("/films")
public class FilmRestController {

  private final GeneralService generalService;

  public FilmRestController(GeneralService generalService) {
    this.generalService = generalService;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteFilm(@PathVariable Long id) {
    Optional<Film> filmOptional = generalService.findFilmById(id);

    if (filmOptional.isPresent()) {
      generalService.deleteFilmAndItsRelations(filmOptional.get());

      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
