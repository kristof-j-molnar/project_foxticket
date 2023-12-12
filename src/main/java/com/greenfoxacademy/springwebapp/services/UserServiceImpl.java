package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean validatePassword(UserRequestDTO userDTO) {
    if (userDTO.getPassword().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  public boolean validateEmail(UserRequestDTO userDTO) {
    if (userDTO.getEmail().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  public boolean validateName(UserRequestDTO userDTO) {
    if (userDTO.getName().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  public boolean validateEmptyDTO(UserRequestDTO userDTO) {
    if (userDTO.getName().isEmpty() && userDTO.getEmail().isEmpty() && userDTO.getPassword().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  public boolean findEmail(String email) {
    Optional<User> foundUser = userRepository.findUserByEmail(email);
    if (foundUser.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  public boolean checkIfPasswordIsGood(String password) {
    if (!password.isEmpty() && password.length() < 8) {
      return false;
    } else {
      return true;
    }
  }

  public User generateUser(UserRequestDTO userRequestDTO) {
    User newUser = new User(userRequestDTO.getName(), userRequestDTO.getEmail(), userRequestDTO.getPassword(), "USER");
    return newUser;
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }
}
