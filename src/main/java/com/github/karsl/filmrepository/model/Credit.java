package com.github.karsl.filmrepository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "credit")
public class Credit {

    @EmbeddedId
    @JsonIgnore
    private CreditID creditID = new CreditID();

    @ManyToOne
    @MapsId("filmId")
    @JoinColumn(name = "film_id", nullable = false)
    // To prevent circular relationship.
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Film film;

    @ManyToOne
    @MapsId("actorId")
    @JoinColumn(name = "actor_id", nullable = false)
    // To prevent circular relationship.
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Actor actor;

    @Column(name = "role", nullable = false)
    private String role;

}

