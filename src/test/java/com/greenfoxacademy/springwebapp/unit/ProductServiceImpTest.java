package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceImpTest {

  @Test
  void getAvailableProducts_ReturnProductListResponseDTO() {
    var repo = Mockito.mock(ProductRepository.class);
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Mockito.when(repo.findAll()).thenReturn(List.of(p1));

    ProductServiceImp productService = new ProductServiceImp(repo);
    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    productDTOs.add(new ProductDTO(1L, "Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", "Jegy"));

    String actual = productService.getAvailableProductsInDTO().getProducts().get(0).getName();
    assertEquals(productDTOs.getProducts().get(0).getName(), actual);
  }
}