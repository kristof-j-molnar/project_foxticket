package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import com.greenfoxacademy.springwebapp.services.ProductTypeService;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImpTest {

  ProductRepository productRepository = Mockito.mock(ProductRepository.class);
  ProductTypeService productTypeService = Mockito.mock(ProductTypeService.class);
  ProductServiceImp productService = new ProductServiceImp(productRepository, productTypeService);

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
  void validateProductEditDTO_WithValidProductEditRequestDTO_ReturnNull() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertNull(productService.validateProductEditDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithNullName_ReturnName() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO(null, 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertEquals("Name", productService.validateProductEditDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithBlankName_ReturnName() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("       ", 1400, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertEquals("Name", productService.validateProductEditDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithNullPrice_ReturnPrice() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", null, "168 hours",
        "Use this pass for a whole week!", 1L);

    assertEquals("Price", productService.validateProductEditDTO(requestDTO));
  }

  @Test
  void validateProductEditDTO_WithNullDescription_ReturnDescription() {
    ProductEditRequestDTO requestDTO = new ProductEditRequestDTO("1 week pass", 1400, "168 hours",
        null, 1L);

    assertEquals("Description", productService.validateProductEditDTO(requestDTO));
  }

  @Test
  void findById_WithNotExistingId_ReturnEmptyOptional() {
    Mockito.when(productRepository.findById(0L)).thenReturn(Optional.empty());
    assertEquals(Optional.empty(), productService.findById(0L));
  }

  @Test
  void editProduct_WithValidInput_ReturnsEditedProduct() {
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

    assertTrue(EqualsBuilder.reflectionEquals(editedProduct, productService.editProduct(product, productEditRequestDTO)));
  }

  @Test
  void editProduct_WithInvalidTypeId_ThrowsExceptionWithCorrectMessage() {
    Product product = new Product("name", 1000, 168, "test product");
    ProductType productType1 = new ProductType("type1");
    productType1.setId(1L);
    product.setType(productType1);

    Mockito.when(productTypeService.findById(2L)).thenReturn(Optional.empty());

    ProductEditRequestDTO productEditRequestDTO = new ProductEditRequestDTO("name2", 1500,
        "120 days", "edited test product", 2L);

    assertThrows(IllegalArgumentException.class, () -> productService.editProduct(product, productEditRequestDTO));
  }
}