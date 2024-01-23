package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class CartController {
  private CartService cartService;
  private UserService userService;

  private UserAuthenticationService userAuthenticationService;

  @Autowired
  public CartController(CartService cartService, UserService userService, UserAuthenticationService userAuthenticationService) {
    this.cartService = cartService;
    this.userService = userService;
    this.userAuthenticationService = userAuthenticationService;
  }

  @RequestMapping(path = "/cart", method = RequestMethod.GET)
  public ResponseEntity<?> getProductsInCart(Authentication auth) {
    try {
      User user = userService.findUserByEmail((userAuthenticationService.getCurrentUserEmail(auth))).orElseThrow(() -> new EntityNotFoundException(("User is invalid")));
      return ResponseEntity.status(200).body(cartService.getProductsInCartDTO(user.getId()));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @RequestMapping(path = "/cart", method = RequestMethod.POST)
  public ResponseEntity<?> addProductToTheCart(Authentication auth, @RequestBody(required = false) ProductAddingRequestDTO productDTO) {
    try {
      if (cartService.isEmptyAddRequest(productDTO)) {
        throw new IllegalArgumentException("Product ID is required");
      }
      User user = userService.findUserByEmail(userAuthenticationService.getCurrentUserEmail(auth)).orElseThrow(() -> new EntityNotFoundException("User is invalid"));
      if (productDTO.getAmount() == null || productDTO.getAmount() == 1) {
        return ResponseEntity.status(200).body(cartService.addProduct(user, productDTO));
      }
      return ResponseEntity.status(200).body(cartService.addMultipleProduct(user, productDTO));
    } catch (EntityNotFoundException | IllegalArgumentException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

