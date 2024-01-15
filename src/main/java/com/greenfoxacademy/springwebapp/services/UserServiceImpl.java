package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.EditProfileDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
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

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserServiceImpl() {
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
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Boolean validatePassword(User user, UserLoginDTO userLoginDTO) {
    return passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
  }

  @Override
  public User editUserInformation(String email, EditProfileDTO editProfileDTO) {
    Optional<User> editableUser = userRepository.findUserByEmail(email);

    if (editableUser.isPresent()) {
      User foundUser = editableUser.get();
      if (!editProfileDTO.getNewPassword().isEmpty()) {
        foundUser.setPassword(passwordEncoder.encode(editProfileDTO.getNewPassword()));
        userRepository.save(foundUser);
      }
      if (!editProfileDTO.getNewEmail().isEmpty()) {
        foundUser.setEmail(editProfileDTO.getNewEmail());
        userRepository.save(foundUser);
      }
      if (!editProfileDTO.getNewName().isEmpty()) {
        foundUser.setName(editProfileDTO.getNewName());
        userRepository.save(foundUser);
      }
      return foundUser;
    }
    throw new NoSuchElementException("The user does not exist");
  }

  @Override
  public boolean checkEditableEmail(String email) {
    Optional<User> foundUser = userRepository.existsByEmail(email);
    if (foundUser.isEmpty()) {
      return true;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void validateEditProfileDTO(EditProfileDTO editProfileDTO) {

    if (editProfileDTO.getNewName().isEmpty() && editProfileDTO.getNewPassword().isEmpty() && editProfileDTO.getNewEmail().isEmpty()) {
      throw new IllegalArgumentException("Name, password, or email are required.");
    } else if (!findEmail(editProfileDTO.getNewEmail())) {
      throw new IllegalArgumentException("Email is already taken.");
    } else if (!checkIfPasswordIsGood(editProfileDTO.getNewPassword())) {
      throw new IllegalArgumentException("Password must be at least 8 characters.");
    }
  }
}

