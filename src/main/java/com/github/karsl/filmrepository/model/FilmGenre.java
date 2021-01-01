package com.github.karsl.filmrepository.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "film_genre")
@Data
public class FilmGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
