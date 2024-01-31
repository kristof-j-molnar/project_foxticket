package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeAddingResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import com.greenfoxacademy.springwebapp.services.ProductTypeServiceImp;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ProductTypeServiceImpTest {

  ProductTypeRepository typeRepository;
  ProductTypeServiceImp productTypeServiceImp;

  @BeforeEach
  public void fieldInitialization() {
    typeRepository = Mockito.mock(ProductTypeRepository.class);
    productTypeServiceImp = new ProductTypeServiceImp(typeRepository);
  }

  @Test
  void addProductType_withRequestBody_returnResponse() {
    ProductType newType = new ProductType("Test pass");
    Mockito.when(typeRepository.save(any())).thenReturn(newType);

    ProductTypeAddingResponseDTO actual = productTypeServiceImp.addProductType(new ProductTypeAddingRequestDTO("Test pass"));

    assertEquals(newType.getId(), actual.getId());
  }

  @Test
  void addProductType_withExistName_returnError() {
    Mockito.when(typeRepository.existsByName("Test pass")).thenReturn(true);

    EntityExistsException exception = assertThrows(EntityExistsException.class,
        () -> productTypeServiceImp.addProductType(new ProductTypeAddingRequestDTO("Test pass")));
    Assertions.assertEquals("Product type name already exists", exception.getMessage());
  }

  @Test
  void addProductType_withEmptyRequestBodyName_returnError() {
    EmptyFieldsException exception = assertThrows(EmptyFieldsException.class,
        () -> productTypeServiceImp.addProductType(new ProductTypeAddingRequestDTO("")));
    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  void addProductType_withEmptyRequestBodyNullName_returnError() {
    EmptyFieldsException exception = assertThrows(EmptyFieldsException.class,
        () -> productTypeServiceImp.addProductType(new ProductTypeAddingRequestDTO(null)));
    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  void addNews_withEmptyRequestBody_returnError() {
    EmptyFieldsException exception = assertThrows(EmptyFieldsException.class,
        (() -> productTypeServiceImp.addProductType(null)));
    Assertions.assertEquals("Name is required", exception.getMessage());
  }
}