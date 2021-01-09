package com.github.karsl.filmrepository.service;

import com.github.karsl.filmrepository.model.Role;
import com.github.karsl.filmrepository.model.User;
import com.github.karsl.filmrepository.repository.RoleRepository;
import com.github.karsl.filmrepository.repository.UserRepository;
import com.github.karsl.filmrepository.security.UserPrincipal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  public LoginServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findUserByUsername(username);

    if (userOptional.isPresent()) {
      return UserPrincipal.from(userOptional.get());
    } else {
      throw new UsernameNotFoundException("Username \"" + username + "\" not found.");
    }
  }

}
