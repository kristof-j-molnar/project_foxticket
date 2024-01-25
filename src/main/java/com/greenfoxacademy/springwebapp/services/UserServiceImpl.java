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
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public boolean isEmailValid(UserRequestDTO userDTO) {
    return !userDTO.getEmail().isEmpty();
  }

  @Override
  public boolean isNameValid(UserRequestDTO userDTO) {
    return !userDTO.getName().isEmpty();
  }

  @Override
  public boolean isUserDTOValid(UserRequestDTO userDTO) {
    return isNameValid(userDTO) || isEmailValid(userDTO) || !userDTO.getPassword().isEmpty();
  }

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public boolean isPasswordValid(String password) {
    return password.length() >= 8;
  }

  @Override
  public User generateUser(UserRequestDTO userRequestDTO) {
    return new User(userRequestDTO.getName(), userRequestDTO.getEmail(), passwordEncoder.encode(userRequestDTO.getPassword()), "USER");
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
  public Optional<ErrorMessageDTO> validateLogin(UserLoginDTO userLoginDTO) {
    if (userLoginDTO.getEmail() == null && userLoginDTO.getPassword() == null) {
      return Optional.of(new ErrorMessageDTO("All fields are required."));
    } else if (userLoginDTO.getPassword() == null) {
      return Optional.of(new ErrorMessageDTO("Password is required."));
    } else if (userLoginDTO.getEmail() == null) {
      return Optional.of(new ErrorMessageDTO("E-mail is required."));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Boolean validatePassword(User user, UserLoginDTO userLoginDTO) {
    return passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
  }

  @Override
  public User editUserInformation(String email, EditProfileDTO editProfileDTO) {
    User editableUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NoSuchElementException("User does not exist!"));

    if (!editProfileDTO.getNewPassword().isEmpty()) {
      editableUser.setPassword(passwordEncoder.encode(editProfileDTO.getNewPassword()));
      userRepository.save(editableUser);
    }
    if (!editProfileDTO.getNewEmail().isEmpty()) {
      editableUser.setEmail(editProfileDTO.getNewEmail());
      userRepository.save(editableUser);
    }
    if (!editProfileDTO.getNewName().isEmpty()) {
      editableUser.setName(editProfileDTO.getNewName());
      userRepository.save(editableUser);
    }
    return editableUser;
  }

  @Override
  public void validateEditProfileDTO(EditProfileDTO editProfileDTO) {

    if (editProfileDTO.getNewName().isEmpty() && editProfileDTO.getNewPassword().isEmpty()
        && editProfileDTO.getNewEmail().isEmpty()) {
      throw new IllegalArgumentException("Name, password, or email are required.");
    } else if (existsByEmail(editProfileDTO.getNewEmail())) {
      throw new IllegalArgumentException("Email is already taken.");
    } else if (!isPasswordValid(editProfileDTO.getNewPassword())) {
      throw new IllegalArgumentException("Password must be at least 8 characters.");
    }
  }
}