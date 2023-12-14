package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;

public interface UserService {

  boolean validatePassword(UserRequestDTO userDTO);

  boolean validateEmail(UserRequestDTO userDTO);

  boolean validateName(UserRequestDTO userDTO);

  boolean validateEmptyDTO(UserRequestDTO userDTO);

  boolean findEmail(String email);

  boolean checkIfPasswordIsGood(String password);

  User generateUser(UserRequestDTO userRequestDTO);

  void saveUser(User user);

}
