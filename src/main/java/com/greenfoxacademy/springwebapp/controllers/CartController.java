package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    Optional<User> optUser = userService.findUserByEmail((userAuthenticationService.getUserEmail(auth)));
    return optUser
        .map(u -> ResponseEntity.status(200).body(cartService.getProductsInCartDTO(u.getId())))
        .orElseThrow(() -> new EntityNotFoundException(("User is invalid")));
  }

  @Transactional
  @RequestMapping(path = "/cart", method = RequestMethod.POST)
  public ResponseEntity<?> addProductToTheCart(Authentication auth, @RequestBody ProductAddingRequestDTO productId) {
    if (productId == null) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Product ID is required."));
    }
    try {
      ResponseEntity<ProductAddingResponseDTO> response = userService.findUserByEmail(userAuthenticationService.getUserEmail(auth))
          .map(user -> productService.getProductById(productId.getProductId())
              .map(product -> {
                user.getCart().addProduct(product);
                cartService.save(user.getCart());
                int amount = cartService.getAmount(user.getCart(), product);
                return ResponseEntity.status(200).body(new ProductAddingResponseDTO(user.getCart().getId(), productId.getProductId(), amount));
              })
              .orElseThrow(() -> new EntityNotFoundException("Product not found")))
          .orElseThrow(() -> new EntityNotFoundException("User not found"));
      return response;
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }
}

