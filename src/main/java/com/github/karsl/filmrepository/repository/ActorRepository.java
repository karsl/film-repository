package com.github.karsl.filmrepository.repository;

import com.github.karsl.filmrepository.model.Actor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ActorRepository extends JpaRepository<Actor, Long> {

  Optional<Actor> findActorByFullName(String fullName);

  // Provided delete function from JpaRepository doesn't work (maybe Hibernate doesn't flush),
  // so I use my own function.
  @Modifying
  @Query("DELETE FROM Actor WHERE id = :id")
  void deleteActorById(Long id);
}
