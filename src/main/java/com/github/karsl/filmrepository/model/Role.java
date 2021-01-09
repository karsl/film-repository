package com.github.karsl.filmrepository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role {

  @Id
  @Column(name = "id", nullable = false)
  public Long id;

  @Column(name = "name", nullable = false, unique = true)
  public String name;

}
