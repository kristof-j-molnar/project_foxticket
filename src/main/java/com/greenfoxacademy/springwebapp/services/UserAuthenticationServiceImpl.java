package com.greenfoxacademy.springwebapp.services;


import com.greenfoxacademy.springwebapp.dtos.MyUserDetailsDTO;
<<<<<<< HEAD
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
=======
import org.springframework.security.core.context.SecurityContextHolder;

>>>>>>> 46acfa8 (Add edited codes after first review)
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
  public UserAuthenticationServiceImpl() {
  }

  @Override
  public boolean hasRole(String role, Authentication authentication) {
    return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_" + role));
  }

  @Override
  public String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
<<<<<<< HEAD
    if (principal instanceof MyUserDetailsDTO myUserDetailsDTO) {
      return myUserDetailsDTO.getEmail();
    } else {
      throw new IllegalArgumentException();
=======
    if (principal instanceof MyUserDetailsDTO) {
      return ((MyUserDetailsDTO) principal).getEmail();
    } else {
      return null;
>>>>>>> 46acfa8 (Add edited codes after first review)
    }
  }
}

