package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.UserAuthenticationService;
import com.greenfoxacademy.springwebapp.services.UserService;
import com.greenfoxacademy.springwebapp.utilities.JwtBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
  private final JwtBuilder jwtBuilder;
  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserAuthenticationService userAuthenticationService;

  @Autowired
  public UserController(UserService userService, UserAuthenticationService userAuthenticationService, JwtBuilder jwtBuilder) {
    this.userService = userService;
    this.userAuthenticationService = userAuthenticationService;
    this.jwtBuilder = jwtBuilder;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessageDTO> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO(e.getMessage()));
  }

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO) {
    if (!userService.validateEmptyDTO(userRequestDTO)) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Name, email and password are required."));
    } else if (!userService.checkIfPasswordIsGood(userRequestDTO.getPassword())) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Password must be at least 8 characters."));
    } else if (!userService.findEmail(userRequestDTO.getEmail())) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Email is already taken."));
    } else if (!userService.validateName(userRequestDTO)) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Name is required."));
    } else if (!userService.validateEmail(userRequestDTO)) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Email is required."));
    } else if (userRequestDTO.getPassword().isEmpty()) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Password is required."));
    } else if (userService.validateEmail(userRequestDTO) && userService.validateName(userRequestDTO) && !userRequestDTO.getPassword().isEmpty()) {
      User newUser = userService.generateUser(userRequestDTO);
      userService.saveUser(newUser);
      return ResponseEntity.status(200).body(new UserResponseDTO(newUser.getId(), newUser.getEmail(), newUser.getRole()));
    } else {
      return ResponseEntity.ok().build();
    }
  }

  @PostMapping(path = "/users/login")
  public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
    Optional<ErrorMessageDTO> error = userService.validateLogin(userLoginDTO);
    if (error.isPresent()) {
      return ResponseEntity.status(400).body(error);
    }

    Optional<User> optionalUser = userService.findUserByEmail(userLoginDTO.getEmail());
    if (optionalUser.isEmpty()) {
      logger.error("user not found");
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Email or password is incorrect"));
    }
    if (!userService.validatePassword(optionalUser.get(), userLoginDTO)) {
      logger.error("wrong password " + optionalUser.get().getPassword() + " " + userLoginDTO.getPassword());
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Email or password is incorrect"));
    } else {
      User user = optionalUser.get();
      SecurityUser securityUser = new SecurityUser(user);
      String jwt = jwtBuilder.generateToken(securityUser);
      return ResponseEntity.status(200).body(new TokenDTO("ok", jwt));
    }
  }

  @GetMapping("/admin")
  public ResponseEntity<?> adminAuthorization() {
    return ResponseEntity.status(200).body("Authorized access");
  }


  @RequestMapping(value = "/users", method = RequestMethod.PATCH)
  public ResponseEntity<?> editUserProfile(@RequestBody EditProfileDTO editProfileDTO, Authentication authentication) {
    try {
      userService.validateEditProfileDTO(editProfileDTO);

      User editedUser = userService.editUserInformation(userAuthenticationService.getCurrentUserEmail(authentication), editProfileDTO);

      return ResponseEntity.status(200).body(new UserResponseDTOWithName(editedUser));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

