package com.github.karsl.filmrepository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "credit")
public class Credit {

  @EmbeddedId
  @JsonIgnore
  private CreditId creditId = new CreditId();

  @ManyToOne
  @MapsId("filmId")
  @NotNull(message = "Film of a credit can't be null")
  @JoinColumn(name = "film_id", nullable = false)
  // To prevent circular relationship.
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Film film;

  @ManyToOne
  @MapsId("actorId")
  @NotNull(message = "Actor of a credit can't be null")
  @JoinColumn(name = "actor_id", nullable = false)
  // To prevent circular relationship.
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Actor actor;

  @Column(name = "role", nullable = false)
  @NotNull(message = "Role can't be null.")
  @NotEmpty(message = "Role can't be empty.")
  private String role;

}

