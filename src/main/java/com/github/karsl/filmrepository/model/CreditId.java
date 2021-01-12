package com.github.karsl.filmrepository.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditId implements Serializable {

  private Long filmId;

  private Long actorId;

}
