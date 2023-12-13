package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.MyUserDetails;
import com.greenfoxacademy.springwebapp.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MyUserDetailsServiceTest {

  @Autowired
  MyUserDetailsService myUserDetailsService;

  @Test
  void checksReturnedValueOfLoadUserById_WithExistingUser() {
    Integer id = 1;
    MyUserDetails myUserDetails = new MyUserDetails(new User("user", "lacika.com", "pass", "User"));
    MyUserDetails actualMyUserDetails = myUserDetailsService.loadUserById(id);
    assertEquals(myUserDetails.getUsername(), actualMyUserDetails.getUsername());
    assertEquals(myUserDetails.getPassword(), actualMyUserDetails.getPassword());
    assertEquals(myUserDetails.getEmail(), actualMyUserDetails.getEmail());
  }
}