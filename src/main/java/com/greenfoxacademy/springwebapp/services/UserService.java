package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.EditProfileDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;

import java.util.Optional;

public interface UserService {

  boolean isEmailValid(UserRequestDTO userDTO);

  boolean isNameValid(UserRequestDTO userDTO);

  boolean isUserDTOValid(UserRequestDTO userDTO);

  boolean existsByEmail(String email);

  boolean isPasswordValid(String password);

  User generateUser(UserRequestDTO userRequestDTO);

  void saveUser(User user);

  Optional<User> findUserByEmail(String email);

  Optional<ErrorMessageDTO> validateLogin(UserLoginDTO userLoginDTO);

  Boolean validatePassword(User user, UserLoginDTO userLoginDTO);

  User editUserInformation(String email, EditProfileDTO editProfileDTO);

  void validateEditProfileDTO(EditProfileDTO editProfileDTO);
}