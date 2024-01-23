package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import com.greenfoxacademy.springwebapp.services.ProductTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceImpTest {

  ProductRepository productRepository;
  ProductTypeService productTypeService;
  ProductServiceImp productService;

  @BeforeEach
  public void fieldInitialization() {
    productRepository = Mockito.mock(ProductRepository.class);
    productTypeService = Mockito.mock(ProductTypeService.class);
    productService = new ProductServiceImp(productRepository, productTypeService);
  }

  @Test
  void getAvailableProducts_ReturnProductListResponseDTO() {
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Mockito.when(productRepository.findAll()).thenReturn(List.of(p1));

    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    productDTOs.add(new ProductDTO(1L, "Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", "Jegy"));

    String actual = productService.getAvailableProductsInDTO().getProducts().get(0).getName();
    assertEquals(productDTOs.getProducts().get(0).getName(), actual);
  }

  @Test
  void validateProductEditDTO_WithValidProductEditRequestDTO_ReturnEmptyOptional() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertEquals(Optional.empty(), productService.validateProductEditRequestDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithNullName_ReturnsOptionalWithCorrectString() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO(null, 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertEquals(Optional.of("Name is required."), productService.validateProductEditRequestDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithBlankName_ReturnsOptionalWithCorrectString() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("       ", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertEquals(Optional.of("Name is required."), productService.validateProductEditRequestDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithNullPrice_ReturnsOptionalWithCorrectString() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", null, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertEquals(Optional.of("Price is required."), productService.validateProductEditRequestDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithNullDescription_ReturnsOptionalWithCorrectString() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        null, 1L);

    assertEquals(Optional.of("Description is required."), productService.validateProductEditRequestDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithBlankNameAndNullDescription_ReturnsOptionalWithCorrectString() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("    ", 1400, "168 hours",
        null, 1L);

    assertEquals(Optional.of("Name and Description are required."), productService.validateProductEditRequestDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithBlankNameNullPriceAndNullDescription_ReturnsOptionalWithCorrectString() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("    ", null, "168 hours",
        null, 1L);

    assertEquals(Optional.of("Name, Price and Description are required."), productService.validateProductEditRequestDTO(requestDTO));
  }

  @Test
  void findById_WithNotExistingId_ReturnEmptyOptional() {
    Mockito.when(productRepository.findById(0L)).thenReturn(Optional.empty());
    assertEquals(Optional.empty(), productService.findById(0L));
  }
}