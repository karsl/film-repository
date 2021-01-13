package com.github.karsl.filmrepository.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.Year;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;


@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
@Entity
@Data
@Table(name = "film")
public class Film {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "title", nullable = false)
  @NotEmpty(message = "Title can't be empty.")
  private String title;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "genre", nullable = false)
  private FilmGenre genre;

  @Column(name = "year", nullable = false)
  @Min(value = 0, message = "Release year can't be negative.")
  private Integer year = Year.now().getValue();

  @ManyToOne
  @JoinColumn(name = "media", nullable = false)
  private MediaType media;

  @ManyToMany
  @JoinTable(
      name = "available_language",
      joinColumns = @JoinColumn(name = "film_id"),
      inverseJoinColumns = @JoinColumn(name = "language_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"film_id", "language_id"}))
  @Size(min = 1)
  private Set<Language> languages;

  @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
  private Set<Credit> credits;

}
