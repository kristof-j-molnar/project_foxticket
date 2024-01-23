package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.CartService;
import com.greenfoxacademy.springwebapp.services.ProductService;
import com.greenfoxacademy.springwebapp.services.UserAuthenticationService;
import com.greenfoxacademy.springwebapp.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class CartController {
  private final CartService cartService;
  private final UserService userService;
  private final ProductService productService;
  private final UserAuthenticationService userAuthenticationService;

  @Autowired
  public CartController(CartService cartService, UserService userService, ProductService productService, UserAuthenticationService userAuthenticationService) {
    this.cartService = cartService;
    this.userService = userService;
    this.productService = productService;
    this.userAuthenticationService = userAuthenticationService;
  }

  @Transactional
  @RequestMapping(path = "/cart", method = RequestMethod.GET)
  public ResponseEntity<?> getProductsInCart(Authentication auth) {
    try {
      User user = userService.findUserByEmail((userAuthenticationService.getCurrentUserEmail(auth))).orElseThrow(() -> new EntityNotFoundException(("User is invalid")));
      return ResponseEntity.status(200).body(cartService.getProductsInCartDTO(user.getId()));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @Transactional
  @RequestMapping(path = "/cart", method = RequestMethod.POST)
  public ResponseEntity<?> addProductToTheCart(Authentication auth, @RequestBody(required = false) ProductAddingRequestDTO productId) {
    if (cartService.isEmptyAddRequest(productId)) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Product ID is required."));
    }
    try {
      User user = userService.findUserByEmail(userAuthenticationService.getCurrentUserEmail(auth)).orElseThrow(() -> new EntityNotFoundException("User is invalid"));
      Product product = productService.getProductById(productId.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product is not found"));
      return ResponseEntity.status(200).body(cartService.addProduct(user, product));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
  @Transactional
  @RequestMapping(path = "/cart/{itemId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> removeProductFromCart(@PathVariable Long itemId, Authentication auth) {
    try {
      User user = userService.findUserByEmail(userAuthenticationService.getCurrentUserEmail(auth))
          .orElseThrow(() -> new EntityNotFoundException("User is invalid"));

      Product product = productService.getProductById(itemId)
          .orElseThrow(() -> new EntityNotFoundException("Product not found in the cart"));

      cartService.removeProduct(user, product);

      return ResponseEntity.status(200).body(new ConfirmationMessageDTO("Item removed from the shopping cart"));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @Transactional
  @RequestMapping(path = "/cart", method = RequestMethod.DELETE)
  public ResponseEntity<?> clearCart(Authentication auth) {
    try {
      User user = userService.findUserByEmail(userAuthenticationService.getCurrentUserEmail(auth))
          .orElseThrow(() -> new EntityNotFoundException("User is invalid"));

      cartService.clearCart(user);

      return ResponseEntity.status(200).body(new ConfirmationMessageDTO("All items removed from the shopping cart"));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}