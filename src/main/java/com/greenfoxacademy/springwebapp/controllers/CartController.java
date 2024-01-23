package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class CartController {
  private CartService cartService;
  private UserService userService;

  private ProductService productService;

  private UserAuthenticationService userAuthenticationService;

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
  @RequestMapping(path = "/cart/addMultiple", method = RequestMethod.POST)
  public ResponseEntity<?> addMultipleProductToTheCart(Authentication auth, @RequestBody(required = false) MultipleProductsAddingRequestDTO productWithAmount) {
    if (cartService.isEmptyMultipleAddRequest(productWithAmount)) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Product ID is required."));
    }
    try {
      User user = userService.findUserByEmail(userAuthenticationService.getCurrentUserEmail(auth)).orElseThrow(() -> new EntityNotFoundException("User is invalid"));
      Product product = productService.getProductById(productWithAmount.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product is not found"));
      return ResponseEntity.status(200).body(cartService.addMultipleProduct(user, product, productWithAmount.getAmount()));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

