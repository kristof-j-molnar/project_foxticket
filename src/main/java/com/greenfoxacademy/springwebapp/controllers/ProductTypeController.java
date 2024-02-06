package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingRequestDTO;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.services.ProductTypeService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/product-type")
public class ProductTypeController {
  private final ProductTypeService productTypeService;

  @Autowired
  public ProductTypeController(ProductTypeService productTypeService) {
    this.productTypeService = productTypeService;
  }


  @PostMapping
  public ResponseEntity<?> addProductType(@RequestBody ProductTypeAddingRequestDTO productType) {
    try {
      return ResponseEntity.status(200).body(productTypeService.addProductType(productType));
    } catch (EmptyFieldsException | EntityExistsException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
