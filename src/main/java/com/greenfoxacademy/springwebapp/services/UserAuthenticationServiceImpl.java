package com.greenfoxacademy.springwebapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

  public UserAuthenticationServiceImpl() {
  }

  public boolean hasRole(Authentication authentication){
      if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))){
        return true;
      } else {
        return false;
      }
  }
}
