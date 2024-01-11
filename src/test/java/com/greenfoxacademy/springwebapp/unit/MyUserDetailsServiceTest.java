package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MyUserDetailsServiceTest {

  @Autowired
  MyUserDetailsService myUserDetailsService;

  @Test
  void checksReturnedValueOfLoadUserById_WithExistingUser() {
    var mock = Mockito.mock(UserRepository.class);
    Integer id = 1;
    User user = new User("user", "lacika.com", "pass", "User");

    Mockito.when(mock.findById(id)).thenReturn(Optional.of(user));
    myUserDetailsService = new MyUserDetailsService(mock);

    String userNameActual = myUserDetailsService.loadUserById(id).getUsername();
    String userEmailActual = myUserDetailsService.loadUserById(id).getEmail();
    String userPasswordActual = myUserDetailsService.loadUserById(id).getPassword();
    assertEquals(user.getName(), userNameActual);
    assertEquals(user.getEmail(), userEmailActual);
    assertEquals(user.getPassword(), userPasswordActual);
  }
}