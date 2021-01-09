package com.github.karsl.filmrepository.controller;

import com.github.karsl.filmrepository.model.*;
import com.github.karsl.filmrepository.service.GeneralService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        model.addAttribute("newFilm", new Film());

        return "films";
    }

    @PostMapping("submitForm")
    @ResponseBody
    public Film processForm(@RequestBody FormSubmission formSubmission) {
        Film film = formSubmission.mapToFilm();

        generalService.mapFieldsWithIDsToObjectsOfFilm(film);

        return generalService.saveFilm(film);
    }

    @PutMapping("/submitForm")
    @ResponseBody
    public Film processUpdateForm(@RequestBody FormSubmission formSubmission) {
        Film film = formSubmission.mapToFilm();

        generalService.mapFieldsWithIDsToObjectsOfFilm(film);
        generalService.updateFilmsCredits(film);

        return generalService.saveFilm(film);
    }

}
