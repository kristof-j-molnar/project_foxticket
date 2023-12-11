package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.UserRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.UserResponseDTO;
import com.greenfoxacademy.springwebapp.models.ErrorMessage;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO) {

        if (!userService.validateEmptyDTO(userRequestDTO)) {
            return ResponseEntity.status(404).body(new ErrorMessage("Name, email and password are required."));
        } else if (!userService.checkIfPasswordIsGood(userRequestDTO.getPassword())) {
            return ResponseEntity.status(404).body(new ErrorMessage("Password must be at least 8 characters."));
        } else if (!userService.findEmail(userRequestDTO.getEmail())) {
            return ResponseEntity.status(404).body(new ErrorMessage("Email is already taken."));
        } else if (!userService.validateName(userRequestDTO)) {
            return ResponseEntity.status(404).body(new ErrorMessage("Name is required."));
        } else if (!userService.validateEmail(userRequestDTO)) {
            return ResponseEntity.status(404).body(new ErrorMessage("Email is required."));
        } else if (!userService.validatePassword(userRequestDTO)) {
            return ResponseEntity.status(404).body(new ErrorMessage("Password is required."));
        } else if (userService.validateEmail(userRequestDTO) && userService.validateName(userRequestDTO) && userService.validatePassword(userRequestDTO)) {
            User newUser = userService.generateUser(userRequestDTO);
            userService.saveUser(newUser);
            return ResponseEntity.status(200).body(new UserResponseDTO(newUser.getId(), newUser.getEmail(), newUser.getRole()));
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
