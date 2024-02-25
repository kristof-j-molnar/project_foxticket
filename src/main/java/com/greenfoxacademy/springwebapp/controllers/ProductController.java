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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {
  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
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

  @RequestMapping(path = "/{productId}", method = RequestMethod.POST)
  public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
    try {
      productService.deleteProductById(productId);
      return ResponseEntity.status(200).build();
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
