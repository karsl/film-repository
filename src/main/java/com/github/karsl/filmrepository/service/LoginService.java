package com.github.karsl.filmrepository.service;

import com.github.karsl.filmrepository.model.Role;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoginService extends UserDetailsService {

  List<Role> getAllRoles();

}
