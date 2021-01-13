package com.github.karsl.filmrepository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@Data
public class Role implements GrantedAuthority {

  @Id
  @Column(name = "id", nullable = false)
  public Long id;

  @Column(name = "name", nullable = false, unique = true)
  @NotEmpty(message = "Name of a role can't be empty.")
  @NotNull(message = "Name of a role can't be null.")
  public String name;

  @Override
  public String getAuthority() {
    return "ROLE_" + name;
  }

}
