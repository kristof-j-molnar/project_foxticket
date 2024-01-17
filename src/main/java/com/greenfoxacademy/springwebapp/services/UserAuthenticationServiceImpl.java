package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

  @Override
  public boolean hasRole(String role, Authentication authentication) {
    return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_" + role));
  }

  @Override
  public String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (principal instanceof SecurityUser myUserDetailsDTO) {
      return myUserDetailsDTO.getEmail();
    } else {
      throw new IllegalArgumentException("Authentication information in unexpected format");
    }
  }
}
