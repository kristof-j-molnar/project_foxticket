package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.EditProfileDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;

import java.util.Optional;

public interface UserService {

  boolean validateEmail(UserRequestDTO userDTO);

  boolean validateName(UserRequestDTO userDTO);

  boolean validateEmptyDTO(UserRequestDTO userDTO);

  boolean findEmail(String email);

  boolean checkIfPasswordIsGood(String password);

  User generateUser(UserRequestDTO userRequestDTO);

  void saveUser(User user);

  Optional<User> findUserByEmail(String email);

  ErrorMessageDTO validateLogin(UserLoginDTO userLoginDTO);

  Boolean validatePassword(User user, UserLoginDTO userLoginDTO);

  User editUserInformation(String email, EditProfileDTO editProfileDTO);

  boolean checkEditableEmail(String email);

  void validateEditProfileDTO(EditProfileDTO editProfileDTO);
}

