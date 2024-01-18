package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.MyUserDetailsDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

  @Override
  public boolean hasRole(String role, Authentication authentication) {
    return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_" + role));
  }

  public String getCurrentUserEmail(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    if (principal instanceof MyUserDetailsDTO user) {
      return (user.getEmail());
    }
    throw new IllegalStateException("Authentication principal is not an instance of MyUserDetailsDTO");
  }
}