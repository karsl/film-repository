package com.github.karsl.filmrepository.repository;

import com.github.karsl.filmrepository.model.Actor;
import com.github.karsl.filmrepository.model.Credit;
import com.github.karsl.filmrepository.model.Film;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CreditRepository extends JpaRepository<Credit, Long> {

  @Query("FROM Credit WHERE actor = :actor")
  List<Credit> getCreditsByActor(Actor actor);

  @Modifying
  @Query("DELETE FROM Credit WHERE actor = :actor AND film = :film")
  void deleteCreditByActorAndFilm(Actor actor, Film film);
}
