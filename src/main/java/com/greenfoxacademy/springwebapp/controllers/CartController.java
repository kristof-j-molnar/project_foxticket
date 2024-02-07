package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ConfirmationMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.CartService;
import com.greenfoxacademy.springwebapp.services.UserAuthenticationService;
import com.greenfoxacademy.springwebapp.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class CartController {
  private final CartService cartService;
  private final UserService userService;
  private final UserAuthenticationService userAuthenticationService;

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
        return ResponseEntity.status(400).body(new ErrorMessageDTO("Product ID is required"));
      }
      return ResponseEntity.status(200).body(cartService.addProduct(productDTO));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @RequestMapping(path = "/cart/{itemId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> removeProductFromCart(@PathVariable Long itemId, Authentication auth) {
    try {
      cartService.removeProduct(itemId, auth);
      return ResponseEntity.status(200).body(new ConfirmationMessageDTO("Item removed from the shopping cart"));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @RequestMapping(path = "/cart", method = RequestMethod.DELETE)
  public ResponseEntity<?> clearCart(Authentication auth) {
    try {
      cartService.clearCart(auth);
      return ResponseEntity.status(200).body(new ConfirmationMessageDTO("All items removed from the shopping cart"));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}