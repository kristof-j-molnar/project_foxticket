package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.EditProfileDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EditProfileUnitTests {
  UserService userService = new UserServiceImpl();

  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  UserRepository userRepository;

  @BeforeEach
  void init() {
    userRepository = Mockito.mock(UserRepository.class);
    passwordEncoder = new BCryptPasswordEncoder();
    userService = new UserServiceImpl(userRepository, passwordEncoder);
  }

  @Test
  void validateEditProfileDTO_ShouldReturnRightErrorMessage_WithEmptyDTO() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("", "", "");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userService.validateEditProfileDTO(editProfileDTO));

    Assertions.assertEquals("Name, password, or email are required.", exception.getMessage());
  }

  @Test
  void validateEditProfileDTO_ShouldReturnRightException_WithWrongEmailField() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass123456");
    User user1 = new User("reka", "reka@gmail.com", "reka12345", "User");
    Mockito.when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userService.validateEditProfileDTO(editProfileDTO));

    Assertions.assertEquals("Email is already taken.", exception.getMessage());
  }

  @Test
  void validateEditProfileDTO_ShouldReturnRightException_WithWrongPasswordField() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userService.validateEditProfileDTO(editProfileDTO));

    Assertions.assertEquals("Password must be at least 8 characters.", exception.getMessage());
  }

  @Test
  void validateEditProfileDTO_ShouldNotReturnException_WithRightInputFields() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass123456");

    assertDoesNotThrow(() -> userService.validateEditProfileDTO(editProfileDTO));
  }

  @Test
  void editUserInformation_ShouldReturnEditedUser_WithRightInputFields() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "pass12345687");
    String email = "ferenczy.reka05@gmail.com";
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", passwordEncoder.encode("reka12345"), "User");
    Mockito.when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    User actualUser = userService.editUserInformation(email, editProfileDTO);
    boolean expectedBoolean = true;
    boolean actualBoolean = passwordEncoder.matches("pass12345687", actualUser.getPassword());
    Assertions.assertEquals(expectedBoolean, actualBoolean);
  }

  @Test
  void editUserInformation_ShouldReturnEditedUser_WithTwoInputFieldDTO() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "reka@gmail.com", "");
    String email = "ferenczy.reka05@gmail.com";
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", passwordEncoder.encode("reka12345"), "User");
    Mockito.when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    User actualUser = userService.editUserInformation(email, editProfileDTO);
    boolean expectedBoolean = true;
    boolean actualBoolean = passwordEncoder.matches("reka12345", actualUser.getPassword());
    Assertions.assertEquals(expectedBoolean, actualBoolean);
  }

  @Test
  void editUserInformation_ShouldReturnEditedUser_WithOneInputFieldDTO() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "", "");
    String email = "ferenczy.reka05@gmail.com";
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", passwordEncoder.encode("reka12345"), "User");
    Mockito.when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    User actualUser = userService.editUserInformation(email, editProfileDTO);
    boolean expectedBoolean = true;
    boolean actualBoolean = passwordEncoder.matches("reka12345", actualUser.getPassword());
    Assertions.assertEquals(expectedBoolean, actualBoolean);
  }

  @Test
  void editUserInformation_ShouldThrowException_WithEmptyEmail() {
    EditProfileDTO editProfileDTO = new EditProfileDTO("testDto", "", "");
    String email = "";
    User user1 = new User("reka", "ferenczy.reka05@gmail.com", "reka12345", "User");
    Mockito.when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

    NoSuchElementException exception = assertThrows(NoSuchElementException.class,
        () -> userService.editUserInformation(email, editProfileDTO));

    Assertions.assertEquals("The user does not exist", exception.getMessage());
  }
}
