package com.github.karsl.filmrepository.controller;

import com.github.karsl.filmrepository.dto.FormSubmission;
import com.github.karsl.filmrepository.model.Film;
import com.github.karsl.filmrepository.model.FilmGenre;
import com.github.karsl.filmrepository.model.Language;
import com.github.karsl.filmrepository.model.MediaType;
import com.github.karsl.filmrepository.service.GeneralService;
import java.util.List;
import javax.persistence.EntityExistsException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class FilmController {

  private final GeneralService generalService;

  public FilmController(GeneralService generalService) {
    this.generalService = generalService;
  }

  @GetMapping
  public String showFilms(Model model) {
    List<Film> allFilms = generalService.getAllFilms();
    List<Language> registeredLanguages = generalService.getRegisteredLanguages();
    List<FilmGenre> registeredGenres = generalService.getRegisteredGenres();
    List<MediaType> supportedMediaTypes = generalService.getSupportedMediaServices();

    model.addAttribute("films", allFilms);
    model.addAttribute("registeredLanguages", registeredLanguages);
    model.addAttribute("registeredGenres", registeredGenres);
    model.addAttribute("supportedMediaTypes", supportedMediaTypes);

    return "films";
  }

  @PostMapping("submitForm")
  public String processForm(@RequestBody FormSubmission formSubmission, Model model) {
    Film film = formSubmission.mapToFilm();

    generalService.mapFieldsWithIdsToObjectsOfFilm(film);

    Film savedFilm = generalService.saveFilm(film);

    model.addAttribute("film", savedFilm);

    return "fragments/films :: film_row";
  }

  @PutMapping("/submitForm")
  public String processUpdateForm(@RequestBody FormSubmission formSubmission, Model model) {
    Film film = formSubmission.mapToFilm();

    generalService.mapFieldsWithIdsToObjectsOfFilm(film);
    generalService.updateFilmsCredits(film);

    Film updatedFilm = generalService.saveFilm(film);

    model.addAttribute("film", updatedFilm);

    return "fragments/films :: film_row";
  }

  // The causes of those exceptions are too broad to put in the global exception handler.
  // Within this controller, they are thrown when multiple actors with same name is submitted.
  @ExceptionHandler({PSQLException.class, IllegalStateException.class, EntityExistsException.class})
  @ResponseBody
  public ResponseEntity<?> handleExceptionSameActorName() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(List.of("Two actors can't have the same name."));
  }

}
