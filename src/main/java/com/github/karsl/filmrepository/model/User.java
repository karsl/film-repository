package com.github.karsl.filmrepository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
// Table name is enclosed with backticks since it's reserved in postgres
@Table(name = "`user`")
@Data
public class User {

  @Id
  @Column(name = "id", nullable = false)
  public Long id;

  @NotEmpty(message = "Username can't be empty.")
  @NotNull(message = "Username can't be null.")
  @Column(name = "username", nullable = false, unique = true)
  public String username;

  @NotEmpty(message = "Password can't be empty.")
  @NotNull(message = "Password can't be null.")
  @Column(name = "password", nullable = false)
  public String password;

  @ManyToOne
  @JoinColumn(name = "role", nullable = false)
  public Role role;

}
