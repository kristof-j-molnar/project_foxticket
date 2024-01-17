package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Cart;
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
    Optional<User> optUser = userService.findUserByEmail((userAuthenticationService.getCurrentUserEmail(auth)));

    try {
      return optUser
          .map(u -> ResponseEntity.status(200).body(cartService.getProductsInCartDTO(u.getId())))
          .orElseThrow(() -> new EntityNotFoundException(("User is invalid")));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @Transactional
  @RequestMapping(path = "/cart", method = RequestMethod.POST)
  public ResponseEntity<?> addProductToTheCart(Authentication auth, @RequestBody(required = false) ProductAddingRequestDTO productId) {
    if (cartService.isEmptyDTO(productId)) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO("Product ID is required."));
    }

    Optional<User> optUser = userService.findUserByEmail(userAuthenticationService.getUserEmail(auth));
    Optional<Product> optProduct = productService.getProductById(productId.getProductId());
    try {
      return optUser.map(user -> optProduct.map(product -> {
        Cart cart = optUser.get().getCart();
        cart.addProduct(optProduct.get());
        cartService.save(cart);
        int amount = cartService.getAmount(cart, optProduct.get());
        return ResponseEntity.status(200).body(new ProductAddingResponseDTO(cart.getId(), optProduct.get().getId(), amount));
      }).orElseThrow(() -> new EntityNotFoundException("Product is not found")))
          .orElseThrow(() -> new EntityNotFoundException("User is invalid"));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

