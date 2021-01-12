package com.github.karsl.filmrepository.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "film_genre")
@Data
public class FilmGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Name of a film genre can't be empty.")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
