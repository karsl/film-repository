package com.github.karsl.filmrepository.security;

import com.github.karsl.filmrepository.model.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor(staticName = "from")
@AllArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {

  private User user;

  public static UserPrincipal from(User user) {
    Objects.requireNonNull(user.getUsername());
    Objects.requireNonNull(user.getPassword());
    Objects.requireNonNull(user.getRole());

    return new UserPrincipal(user);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(user.role);
  }

  @Override
  public String getPassword() {
    return user.password;
  }

  @Override
  public String getUsername() {
    return user.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
