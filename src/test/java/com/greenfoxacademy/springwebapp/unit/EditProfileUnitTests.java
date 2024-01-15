package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.EditProfileDTO;
import com.greenfoxacademy.springwebapp.dtos.MyUserDetailsDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EditProfileUnitTests {

  UserService userService = new UserServiceImpl();

  UserAuthenticationService userAuthenticationService = new UserAuthenticationServiceImpl();

  @Test
  void validateEditProfileDTO_ShouldReturnRightErrorMessage_WithEmptyDTO() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("", "", "");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userService.validateEditProfileDTO(editProfileDTO));

    Assertions.assertEquals("Name, password, or email are required.", exception.getMessage());


  }

  @Test
  void validateEditProfileDTO_ShouldReturnRightException_WithWrongEmailField(){
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass123456");
    var repo = Mockito.mock(UserRepository.class);
    User user1 = new User("reka", "reka@gmail.com", "reka12345", "User");
    Mockito.when(repo.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    UserServiceImpl userServiceImpl = new UserServiceImpl(repo);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userServiceImpl.validateEditProfileDTO(editProfileDTO));

    Assertions.assertEquals("Email is already taken.", exception.getMessage());

  }

  @Test
  void validateEditProfileDTO_ShouldReturnRightException_WithWrongPasswordField(){
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass");
    var repo = Mockito.mock(UserRepository.class);

    UserServiceImpl userServiceImpl = new UserServiceImpl(repo);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userServiceImpl.validateEditProfileDTO(editProfileDTO));

    Assertions.assertEquals("Password must be at least 8 characters.", exception.getMessage());
  }

  @Test
  void validateEditProfileDTO_ShouldNotReturnException_WithRightInputFields(){
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass123456");
    var repo = Mockito.mock(UserRepository.class);

    UserServiceImpl userServiceImpl = new UserServiceImpl(repo);
    assertDoesNotThrow(() -> userServiceImpl.validateEditProfileDTO(editProfileDTO));
  }

  @Test
  void editUserInformation_ShouldReturnEditedUser_WithRightInputFields() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass12345687");
    String email = "ferenczy.reka05@gmail.com";
    var repo = Mockito.mock(UserRepository.class);
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", "reka12345", "User");
    Mockito.when(repo.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    UserServiceImpl userServiceImpl = new UserServiceImpl(repo);
    User expectedUser = new User("testDto", "reka@gmail.com", "pass12345687", "User");
    User actualUser = userServiceImpl.editUserInformation(email, editProfileDTO);
    Assertions.assertEquals(expectedUser, actualUser);
  }

  @Test
  void editUserInformation_ShouldReturnEditedUser_WithTwoInputFieldDTO() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "");
    String email = "ferenczy.reka05@gmail.com";
    var repo = Mockito.mock(UserRepository.class);
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", "reka12345", "User");
    Mockito.when(repo.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    UserServiceImpl userServiceImpl = new UserServiceImpl(repo);
    User expectedUser = new User("testDto", "reka@gmail.com", "reka12345", "User");
    User actualUser = userServiceImpl.editUserInformation(email, editProfileDTO);
    Assertions.assertEquals(expectedUser, actualUser);
  }

  @Test
  void editUserInformation_ShouldReturnEditedUser_WithOneInputFieldDTO() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "", "");
    String email = "ferenczy.reka05@gmail.com";
    var repo = Mockito.mock(UserRepository.class);
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", "reka12345", "User");
    Mockito.when(repo.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    UserServiceImpl userServiceImpl = new UserServiceImpl(repo);
    User expectedUser = new User("testDto", "ferenczy.reka05@gmail.com", "reka12345", "User");
    User actualUser = userServiceImpl.editUserInformation(email, editProfileDTO);
    Assertions.assertEquals(expectedUser, actualUser);
  }

  @Test
  void editUserInformation_ShouldThrowException_WithEmptyEmail() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "", "");
    String email = "";
    var repo = Mockito.mock(UserRepository.class);
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", "reka12345", "User");
    Mockito.when(repo.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    UserServiceImpl userServiceImpl = new UserServiceImpl(repo);
    NoSuchElementException exception = assertThrows(NoSuchElementException.class,
        () -> userServiceImpl.editUserInformation(email, editProfileDTO));

    Assertions.assertEquals("The user does not exist", exception.getMessage());

  }

  @Test
  void getCurrentUserEmail_ShouldReturnEmail_WithValidUser() {
    String role = "User";
    MyUserDetailsDTO myUserDetailsDTO = new MyUserDetailsDTO(new User("reka", "reka@gmail.com", "reka12345", role));

    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);
    Collection<? extends GrantedAuthority> authorities = Arrays.asList(authority);
    Authentication authentication = new UsernamePasswordAuthenticationToken(myUserDetailsDTO.getUsername(), myUserDetailsDTO.getPassword(), authorities);

    String expectedEmail = myUserDetailsDTO.getEmail();
    String actualEmail = userAuthenticationService.getCurrentUserEmail();
    Assertions.assertEquals(expectedEmail, actualEmail);
  }

}
