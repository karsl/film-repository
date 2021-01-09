package com.github.karsl.filmrepository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

  @Id
  @Column(name = "id", nullable = false)
  public Long id;

  @Column(name = "username", nullable = false, unique = true)
  public String username;

  @Column(name = "password", nullable = false)
  public String password;

  @ManyToOne
  @JoinColumn(name = "role", nullable = false)
  public Role role;

}
