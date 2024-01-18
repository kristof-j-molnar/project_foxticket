package com.greenfoxacademy.springwebapp.services;

import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {

  boolean hasRole(String role, Authentication authentication);

  String getCurrentUserEmail(Authentication authentication);

}
