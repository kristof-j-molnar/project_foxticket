package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.MyUserDetailsDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.CartServiceImp;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import com.greenfoxacademy.springwebapp.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class CartController {
  private CartServiceImp cartServiceImp;
  private UserServiceImpl userServiceImpl;

  @Autowired
  public CartController(CartServiceImp cartServiceImp, UserServiceImpl userServiceImpl) {
    this.cartServiceImp = cartServiceImp;
    this.userServiceImpl = userServiceImpl;
  }

  @RequestMapping(path = "/cart", method = RequestMethod.GET)
  public ResponseEntity<CartDTO> getProductsInCart() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = auth.getPrincipal();
    if (principal instanceof MyUserDetailsDTO) {
      User user = userServiceImpl.findUserByEmail(((MyUserDetailsDTO) principal).getEmail()).orElse(null);
      if (user != null) {
        return ResponseEntity.status(200).body(cartServiceImp.getProductsInCartDTO(user.getId()));
      }
    }
    return null;
  }
}

