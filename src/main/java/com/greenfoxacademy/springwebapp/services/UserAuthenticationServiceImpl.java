package com.greenfoxacademy.springwebapp.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

  public UserAuthenticationServiceImpl() {
  }

  public boolean hasRole(String role, Authentication authentication){
      return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase(role));
  }
}
