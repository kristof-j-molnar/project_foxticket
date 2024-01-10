package com.greenfoxacademy.springwebapp.services;


import com.greenfoxacademy.springwebapp.dtos.MyUserDetailsDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

  public UserAuthenticationServiceImpl() {
  }

  public boolean hasRole(String role, Authentication authentication) {
    return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_" + role));
  }

  @Override
  public String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (principal instanceof MyUserDetailsDTO myUserDetailsDTO) {
      return myUserDetailsDTO.getEmail();
    } else {
      throw new IllegalArgumentException();
    }
  }
}

