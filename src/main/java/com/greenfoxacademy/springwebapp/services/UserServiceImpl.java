package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public boolean validateEmail(UserRequestDTO userDTO) {
    if (userDTO.getEmail().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public boolean validateName(UserRequestDTO userDTO) {
    if (userDTO.getName().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public boolean validateEmptyDTO(UserRequestDTO userDTO) {
    if (userDTO.getName().isEmpty() && userDTO.getEmail().isEmpty() && userDTO.getPassword().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public boolean findEmail(String email) {
    Optional<User> foundUser = userRepository.findUserByEmail(email);
    if (foundUser.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean checkIfPasswordIsGood(String password) {
    if (!password.isEmpty() && password.length() < 8) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public User generateUser(UserRequestDTO userRequestDTO) {
    User newUser = new User(userRequestDTO.getName(), userRequestDTO.getEmail(), passwordEncoder.encode(userRequestDTO.getPassword()), "USER");
    return newUser;
  }

  @Override
  public void saveUser(User user) {
    userRepository.save(user);
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Override
  public ErrorMessageDTO validateLogin(UserLoginDTO userLoginDTO) {
    if (userLoginDTO.getEmail() == null && userLoginDTO.getPassword() == null) {
      return new ErrorMessageDTO("All fields are required.");
    } else if (userLoginDTO.getPassword() == null) {
      return new ErrorMessageDTO("Password is required.");
    } else if (userLoginDTO.getEmail() == null) {
      return new ErrorMessageDTO("E-mail is required.");
    } else {
      return null;
    }
  }

  @Override
  public Boolean validatePassword(User user, UserLoginDTO userLoginDTO) {
    return passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
  }
}