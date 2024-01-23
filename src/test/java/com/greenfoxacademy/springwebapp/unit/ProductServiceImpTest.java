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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThrows;

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
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", false);
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Mockito.when(productRepository.findAll()).thenReturn(List.of(p1));

    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    productDTOs.add(new ProductDTO(1L, "Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", "Jegy", false));

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

  @Test
  void deleteProductById_setsProductToDeleted() {
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", false);
    p1.setId(1L);
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Mockito.when(productRepository.findById(p1.getId())).thenReturn(Optional.of(p1));

    Product expectedProduct = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", true);
    Product actualProduct = productService.deleteProductById(p1.getId());
    assertEquals(expectedProduct.isDeleted(), actualProduct.isDeleted());
  }

  @Test
  void deleteProductById_returnsCorrectErrorMessageWithNonExistentProduct() {
    Product p1 = new Product();
    p1.setId(2L);
    ProductType t1 = new ProductType();
    t1.addProduct(p1);
    Mockito.when(productRepository.findById(p1.getId())).thenThrow(new NoSuchElementException("The product does not exist!"));

    NoSuchElementException exception = assertThrows(NoSuchElementException.class,
        () -> productService.deleteProductById(p1.getId()));

    Assertions.assertEquals("The product does not exist!", exception.getMessage());
  }

  @Test
  void deleteProductById_returnsCorrectErrorMessageWithAlreadyDeletedProduct() {
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!", true);
    p1.setId(1L);
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Mockito.when(productRepository.findById(p1.getId())).thenReturn(Optional.of(p1));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> productService.deleteProductById(p1.getId()));

    assertEquals("The product is already deleted!", exception.getMessage());
  }
}