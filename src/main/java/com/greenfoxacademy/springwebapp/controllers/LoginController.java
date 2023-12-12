package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.CustomErrorDTO;
import com.greenfoxacademy.springwebapp.dtos.MyUserDetails;
import com.greenfoxacademy.springwebapp.dtos.TokenDTO;
import com.greenfoxacademy.springwebapp.dtos.UserLoginDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.utilities.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping(path = "/api/users/login")
  public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
    if (userLoginDTO.getEmail() == null && userLoginDTO.getPassword() == null) {
      return ResponseEntity.status(400).body(new CustomErrorDTO("All fields are required."));
    }
    if (userLoginDTO.getPassword() == null) {
      return ResponseEntity.status(400).body(new CustomErrorDTO("Password is required."));
    }
    if (userLoginDTO.getEmail() == null) {
      return ResponseEntity.status(400).body(new CustomErrorDTO("E-mail is required."));
    }

    Optional<User> optionalUser = userRepository.findUserByEmail(userLoginDTO.getEmail());
    if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(userLoginDTO.getPassword())) {
      return ResponseEntity.status(404).body(new CustomErrorDTO("Email or password is incorrect"));
    } else {

      User user = optionalUser.get();
      MyUserDetails myUserDetails = new MyUserDetails(user);
      String jwt = jwtUtil.generateToken(myUserDetails);
      return ResponseEntity.status(200).body(new TokenDTO("ok", jwt));
    }
  }
}