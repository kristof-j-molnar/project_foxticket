package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.ProductNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.ProductTypeNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.UniqueNameViolationException;
import com.greenfoxacademy.springwebapp.services.ProductService;
import com.greenfoxacademy.springwebapp.services.UserAuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {
  private final ProductService productService;
  private UserAuthenticationService userAuthenticationService;

  public ProductController(ProductService productService, UserAuthenticationService userAuthenticationService) {
    this.productService = productService;
    this.userAuthenticationService = userAuthenticationService;
  }


  @GetMapping
  public ResponseEntity<ProductListResponseDTO> getAvailableProducts() {
    return ResponseEntity.status(200).body(productService.getAvailableProductsInDTO());
  }

  @PatchMapping(path = "/{productId}")
  public ResponseEntity<?> editProduct(@PathVariable Long productId, @RequestBody ProductEditRequestDTO productEditRequestDTO) {
    try {
      ProductEditResponseDTO responseDTO = productService.editProduct(productId, productEditRequestDTO);
      return ResponseEntity.status(200).body(responseDTO);
    } catch (EmptyFieldsException | UniqueNameViolationException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    } catch (ProductNotFoundException | ProductTypeNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @RequestMapping(path = "/products/{productId}", method = RequestMethod.POST)
  public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
    try {
      productService.deleteProductById(productId);
      return ResponseEntity.status(200).build();
    } catch (NoSuchElementException | IllegalArgumentException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
