package com.github.karsl.filmrepository.service;

import com.github.karsl.filmrepository.model.Actor;
import com.github.karsl.filmrepository.model.Film;
import com.github.karsl.filmrepository.model.FilmGenre;
import com.github.karsl.filmrepository.model.Language;
import com.github.karsl.filmrepository.model.MediaType;
import java.util.List;
import java.util.Optional;

public interface GeneralService {

  List<Film> getAllFilms();

  Film saveFilm(Film newFilm);

  /**
   * Returns registered (currently present in the database) languages. Each of these languages is
   * the language of one or many films.
   *
   * @return registered languages, sorted by their name in ascending order.
   */
  List<Language> getRegisteredLanguages();


  /**
   * Returns registered (currently present in the database) film genres. Each of these genres is the
   * genre of one or many films.
   *
   * @return registered genres, sorted by their name in ascending order.
   */
  List<FilmGenre> getRegisteredGenres();

  /**
   * Returns supported media service types of films.
   *
   * @return support media service types, sorted by their name in ascending order
   */
  List<MediaType> getSupportedMediaServices();

  Optional<Language> findLanguageById(Long id);

  Optional<FilmGenre> findFilmGenreById(Long id);

  Optional<MediaType> findMediaTypeById(Long id);

  /**
   * Delete actor from database if he/she doesn't appear in credits for any film.
   *
   * @param actor actor to be checked if orphan.
   */
  void deleteActorIfOrphan(Actor actor);

  Optional<Film> findFilmById(Long id);

  /**
   * Delete film, its credits and orphan actors form database.
   *
   * @param film film to be deleted.
   */
  void deleteFilmAndItsRelations(Film film);

  /**
   * Assuming the passed film object's languages', media's, genre's ID fields and credits' full name
   * has been set, fills the rest of their fields (e.g. language name, media name) according to the
   * IDs and credits' full name.
   *
   * @param film the film object that described above
   * @return the film object whose fields are updated according to their IDs and credits' full name.
   */
  Film mapFieldsWithIdsToObjectsOfFilm(Film film);

  /**
   * Updates a film's credits by removing credits if they don't participate anymore or adding new
   * credits.
   *
   * <p>When a new credit is added, a new actor is also added if the actor is new too.
   * When an actor is doesn't appear in credits for any film, he/she is removed too.
   *
   * @param film film with <b>new</b> credits
   * @return passed film object but actor IDs are set for new credits.
   */
  Film updateFilmsCredits(Film film);

}
