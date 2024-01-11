package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.services.ProductService;
import com.greenfoxacademy.springwebapp.services.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {
  private final ProductService productService;
  private final ProductTypeService productTypeService;

  @Autowired
  public ProductController(ProductService productService, ProductTypeService productTypeService) {
    this.productService = productService;
    this.productTypeService = productTypeService;
  }

  @GetMapping
  public ResponseEntity<ProductListResponseDTO> getAvailableProducts() {
    return ResponseEntity.status(200).body(productService.getAvailableProductsInDTO());
  }

  @RequestMapping(path = "/{productId}", method = RequestMethod.PATCH)
  public ResponseEntity<?> editProduct(@PathVariable Long productId, ProductEditRequestDTO productEditRequestDTO) {

    // check for empty or blank fields
    String emptyField = productService.validateProductEditDTO(productEditRequestDTO);
    if (emptyField != null) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(emptyField + " is required."));
    }

    // get the product to edit
    Optional<Product> productOptional = productService.findById(productId);
    Product productToEdit;
    if (productOptional.isEmpty()) {
      return ResponseEntity.status(404).build();
    } else {
      productToEdit = productOptional.get();
    }

    // check for the name of the product if it's unique or not
    if (!productToEdit.getName().equals(productEditRequestDTO.getName())
        && productService.findByName(productEditRequestDTO.getName())) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO("Product name already exists."));
    }

    // check for valid product type
    if (productToEdit.getType().getId() != productEditRequestDTO.getTypeId() &&
        productTypeService.findById(productEditRequestDTO.getTypeId()).isEmpty()) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO("ProductType does not exist."));
    }

    //change product fields in database
    Product editedProduct = productService.editProduct(productToEdit, productEditRequestDTO);


    return null;
  }
}