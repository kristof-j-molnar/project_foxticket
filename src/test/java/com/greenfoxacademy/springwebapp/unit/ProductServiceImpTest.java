package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.ProductTypeNotFoundException;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import com.greenfoxacademy.springwebapp.services.ProductTypeService;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  void modifyProduct_WithValidInput_ReturnsEditedProduct() throws ProductTypeNotFoundException {
    Product product = new Product("name", 1000, 168, "test product");
    ProductType productType1 = new ProductType("type1");
    productType1.setId(1L);
    product.setType(productType1);

    ProductType productType2 = new ProductType("type2");
    productType2.setId(2L);
    Mockito.when(productTypeService.findById(2L)).thenReturn(Optional.of(productType2));

    ProductEditRequestDTO productEditRequestDTO = new ProductEditRequestDTO("name2", 1500,
        "120 days", "edited test product", 2L);
    Product editedProduct = new Product("name2", 1500, 120, "edited test product");
    editedProduct.setType(productType2);

    assertTrue(EqualsBuilder.reflectionEquals(editedProduct, productService.modifyProduct(product, productEditRequestDTO)));
  }

  @Test
  void modifyProduct_WithInvalidTypeId_ThrowsExceptionWithCorrectMessage() {
    Product product = new Product("name", 1000, 168, "test product");
    ProductType productType1 = new ProductType("type1");
    productType1.setId(1L);
    product.setType(productType1);

    Mockito.when(productTypeService.findById(2L)).thenReturn(Optional.empty());

    ProductEditRequestDTO productEditRequestDTO = new ProductEditRequestDTO("name2", 1500,
        "120 days", "edited test product", 2L);

    assertThrows(ProductTypeNotFoundException.class, () -> productService.modifyProduct(product, productEditRequestDTO));
  }
}