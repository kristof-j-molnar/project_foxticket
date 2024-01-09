package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.UserService;
import com.greenfoxacademy.springwebapp.utilities.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

  private JwtUtil jwtUtil;
  private UserService userService;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  public UserController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
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
    ErrorMessageDTO error = userService.validateLogin(userLoginDTO);
    if (error != null) {
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
      MyUserDetailsDTO myUserDetailsDTO = new MyUserDetailsDTO(user);
      String jwt = jwtUtil.generateToken(myUserDetailsDTO);
      return ResponseEntity.status(200).body(new TokenDTO("ok", jwt));
    }
  }

  @GetMapping("/admin")
  public ResponseEntity<?> adminAuthorization(UserRequestDTO userRequestDTO) {
    if (!Objects.equals(userRequestDTO.getRole(), "ADMIN")){
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Unauthorized access"));
    } else {
      return ResponseEntity.status(200).body("Authorized access");
    }
  }

}
