package com.github.karsl.filmrepository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "language")
@Data
public class Language {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotEmpty(message = "Name of a language can't be empty.")
  @NotNull(message = "Name of a language can't be null.")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

}
