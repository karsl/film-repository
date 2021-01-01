package com.github.karsl.filmrepository.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.Year;
import java.util.Set;

@Entity
@Data
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "genre", nullable = false)
    private FilmGenre genre;

    @Column(name = "year", nullable = false)
    @Min(0)
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
    private Set<Language> languages;

    @OneToMany(mappedBy = "film")
    private Set<Credit> credits;

}
