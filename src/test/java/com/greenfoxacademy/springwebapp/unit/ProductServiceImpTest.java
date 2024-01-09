package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImpTest {

  @Autowired
  private ProductServiceImp productServiceImp;

  @Test
  void getAvailableProducts_ReturnProductListResponseDTO() {
    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    productDTOs.add(new ProductDTO(1L, "Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", "Jegy"));
    productDTOs.add(new ProductDTO(2L, "Vonaljegy", 360, 90, "90 perces vonaljegy BP agglomerációjában!", "Jegy"));
    ProductListResponseDTO actualProductDTOs = productServiceImp.getAvailableProductsInDTO();
    assertEquals(productDTOs.getProducts().get(1).getId(), actualProductDTOs.getProducts().get(1).getId());
    assertEquals(productDTOs.getProducts().get(1).getDescription(), actualProductDTOs.getProducts().get(1).getDescription());
  }
}