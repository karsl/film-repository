package com.github.karsl.filmrepository.service;

import com.github.karsl.filmrepository.model.Actor;
import com.github.karsl.filmrepository.model.CreditId;
import com.github.karsl.filmrepository.model.Film;
import com.github.karsl.filmrepository.model.FilmGenre;
import com.github.karsl.filmrepository.model.Language;
import com.github.karsl.filmrepository.model.MediaType;
import com.github.karsl.filmrepository.repository.ActorRepository;
import com.github.karsl.filmrepository.repository.CreditRepository;
import com.github.karsl.filmrepository.repository.FilmGenreRepository;
import com.github.karsl.filmrepository.repository.FilmRepository;
import com.github.karsl.filmrepository.repository.LanguageRepository;
import com.github.karsl.filmrepository.repository.MediaTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


@Service
public class GeneralServiceImpl implements GeneralService {

  private final LanguageRepository languageRepository;
  private final FilmGenreRepository filmGenreRepository;
  private final MediaTypeRepository mediaTypeRepository;
  private final ActorRepository actorRepository;
  private final CreditRepository creditRepository;
  private final FilmRepository filmRepository;

  public GeneralServiceImpl(LanguageRepository languageRepository,
      FilmGenreRepository filmGenreRepository,
      MediaTypeRepository mediaTypeRepository, ActorRepository actorRepository,
      CreditRepository creditRepository, FilmRepository filmRepository) {
    this.languageRepository = languageRepository;
    this.filmGenreRepository = filmGenreRepository;
    this.mediaTypeRepository = mediaTypeRepository;
    this.actorRepository = actorRepository;
    this.creditRepository = creditRepository;
    this.filmRepository = filmRepository;
  }

  @Override
  public Optional<Language> findLanguageById(Long id) {
    return languageRepository.findById(id);
  }

  @Override
  public Optional<FilmGenre> findFilmGenreById(Long id) {
    return filmGenreRepository.findById(id);
  }

  @Override
  public Optional<MediaType> findMediaTypeById(Long id) {
    return mediaTypeRepository.findById(id);
  }

  @Transactional
  @Override
  public void deleteActorIfOrphan(Actor actor) {
    if (creditRepository.getCreditsByActor(actor).isEmpty()) {
      actorRepository.deleteActorById(actor.getId());
    }
  }

  @Override
  public Optional<Film> findFilmById(Long id) {
    return filmRepository.findById(id);
  }

  @Override
  @Transactional
  public void deleteFilmAndItsRelations(Film film) {
    filmRepository.delete(film);
    film.getCredits().forEach(c -> deleteActorIfOrphan(c.getActor()));
  }

  @Override
  public List<Film> getAllFilms() {
    return filmRepository.findAll();
  }

  @Override
  public Film saveFilm(Film newFilm) {
    return filmRepository.save(newFilm);
  }

  @Override
  public List<Language> getRegisteredLanguages() {
    return filmRepository.getAllLanguages();
  }

  @Override
  public List<FilmGenre> getRegisteredGenres() {
    return filmRepository.getAllGenres();
  }

  @Override
  public List<MediaType> getSupportedMediaServices() {
    return filmRepository.getAllMediaTypes();
  }

  @Override
  public Film mapFieldsWithIdsToObjectsOfFilm(@NonNull Film film) {
    film.setCredits(film.getCredits().stream().map(c -> {
      actorRepository.findActorByFullName(c.getActor().getFullName())
          .ifPresent(a -> c.setActor(a));
      return c;
    }).collect(Collectors.toSet()));

    film.setLanguages(film.getLanguages().stream().map(l -> {
      Optional<Language> languageWithId = findLanguageById(l.getId());
      return languageWithId.orElse(l);
    }).collect(Collectors.toSet()));

    findMediaTypeById(film.getMedia().getId()).ifPresent(film::setMedia);

    findFilmGenreById(film.getGenre().getId()).ifPresent(film::setGenre);

    return film;
  }

  @Transactional
  @Override
  public Film updateFilmsCredits(Film film) {
    film.setCredits(film.getCredits().stream().map(c -> {
      // We know the film when updating film.
      c.setFilm(film);

      actorRepository.findActorByFullName(c.getActor().getFullName())
          .ifPresentOrElse(
              // If the actor can be found in the database, map the current actor to the actor
              // in the database.
              // This is the case for credits whose actor didn't change.
              c::setActor,
              // Otherwise, save the new actor to the database.
              () -> c.setActor(actorRepository.save(c.getActor())));

      // At this stage, all actors are fetched from database or, if new, saved in database.
      // Therefore we know all actor and film IDs.
      c.setCreditId(new CreditId(film.getId(), c.getActor().getId()));

      return c;
    }).collect(Collectors.toSet()));

    Set<?> newIds = film.getCredits().stream().map(c -> c.getActor().getId())
        .collect(Collectors.toSet());

    // This get() call here is safe since the film guaranteed to be in database.
    findFilmById(film.getId()).get().getCredits()
        .stream()
        // Delete credits who is not a credit after the film is updated.
        .filter(oldCredit -> !newIds.contains(oldCredit.getActor().getId()))
        .forEach(c -> {
          creditRepository.deleteCreditByActorAndFilm(c.getActor(), c.getFilm());
          deleteActorIfOrphan(c.getActor());
        });

    return film;
  }
}
