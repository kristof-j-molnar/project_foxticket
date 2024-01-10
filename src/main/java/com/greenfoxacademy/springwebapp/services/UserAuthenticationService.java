package com.greenfoxacademy.springwebapp.services;

<<<<<<< HEAD
import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {

  boolean hasRole(String role, Authentication authentication);

=======
public interface UserAuthenticationService {

   Object getCurrentUser();

   String findCurrentUserEmail(Object principal);
>>>>>>> 8f3f0d7 (Add codes for profile endpoint before postman tests)
}
