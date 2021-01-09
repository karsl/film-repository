package com.github.karsl.filmrepository.service;

import com.github.karsl.filmrepository.model.Role;
import com.github.karsl.filmrepository.repository.RoleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

  private final RoleRepository roleRepository;

  public LoginServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

}
