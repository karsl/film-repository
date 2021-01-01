package com.github.karsl.filmrepository.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CreditID implements Serializable {

    private Long filmId;

    private Long actorId;

}
