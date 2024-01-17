package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.services.ProductService;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class ProductController {

  private ProductService productService;

  @Autowired
  public ProductController(ProductServiceImp productServiceImp) {
    this.productService = productServiceImp;
  }

  @RequestMapping(path = "/products", method = RequestMethod.GET)
  public ResponseEntity<ProductListResponseDTO> getAvailableProducts() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = auth.getPrincipal();
    return ResponseEntity.status(200).body(productService.getAvailableProductsInDTO());
  }
}
