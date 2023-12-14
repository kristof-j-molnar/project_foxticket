package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class ProductController {

  private ProductServiceImp productServiceImp;

  @Autowired
  public ProductController(ProductServiceImp productServiceImp) {
    this.productServiceImp = productServiceImp;
  }

  @RequestMapping(path = "/products", method = RequestMethod.GET)
  public ResponseEntity<ProductListResponseDTO> getAvailableProducts() {
    return ResponseEntity.status(200).body(productServiceImp.getAvailableProductsInDTO());
  }
}
