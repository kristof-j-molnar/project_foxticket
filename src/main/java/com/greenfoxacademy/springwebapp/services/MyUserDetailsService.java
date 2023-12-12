package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.MyUserDetails;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return null;
  }

  public MyUserDetails loadUserById(Integer userId) {
    Optional<User> foundUser = userRepository.findById(userId);
    return foundUser.map(MyUserDetails::new).orElseThrow(() -> new EntityNotFoundException("User ID not found :" + userId));
  }
}