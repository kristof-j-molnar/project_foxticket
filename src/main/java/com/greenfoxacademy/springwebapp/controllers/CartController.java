package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.MyUserDetailsDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.CartService;
import com.greenfoxacademy.springwebapp.services.ProductService;
import com.greenfoxacademy.springwebapp.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @Autowired
  public CartController(CartService cartService, UserService userService, ProductService productService) {
    this.cartService = cartService;
    this.userService = userService;
    this.productService = productService;
  }

  @RequestMapping(path = "/cart", method = RequestMethod.GET)
  public ResponseEntity<?> getProductsInCart() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = auth.getPrincipal();
    if (principal instanceof MyUserDetailsDTO) {
      Optional<User> optUser = userService.findUserByEmail(((MyUserDetailsDTO) principal).getEmail());
      return optUser
          .map(u -> ResponseEntity.status(200).body(cartService.getProductsInCartDTO(u.getId())))
          .orElseThrow(() -> new EntityNotFoundException(("User is invalid")));
    }
    return ResponseEntity.status(404).build();
  }

  @RequestMapping(path = "/cart", method = RequestMethod.POST)
  public ResponseEntity<?> addProductToTheCart(@RequestBody ProductAddingRequestDTO productId) {
    if (productId == null) {
      return ResponseEntity.status(404).body(new RuntimeException("Product ID is required."));
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = auth.getPrincipal();
    if (principal instanceof MyUserDetailsDTO) {
      Optional<User> optUser = userService.findUserByEmail(((MyUserDetailsDTO) principal).getEmail());

      Optional<Product> product = productService.getProductById(productId.getProductId());
      optUser.ifPresent(user -> product.ifPresent(p -> user.getCart().addProduct(p)));
      cartService.save(optUser.get().getCart());
      return ResponseEntity.status(200).body(new ProductAddingResponseDTO(optUser.get().getCart().getId(), productId.getProductId()));
    }
    return ResponseEntity.status(404).build();
  }
}

