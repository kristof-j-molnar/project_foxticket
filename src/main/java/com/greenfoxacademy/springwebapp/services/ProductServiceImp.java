package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.EmptyFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.ProductNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.ProductTypeNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.UniqueNameViolationException;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

  private final ProductRepository productRepository;
  private final ProductTypeService productTypeService;

  @Autowired
  public ProductServiceImp(ProductRepository productRepository, ProductTypeService productTypeService) {
    this.productRepository = productRepository;
    this.productTypeService = productTypeService;
  }

  public static Optional<String> getErrorMessageByMissingFields(List<String> missingFields) {
    if (missingFields.isEmpty()) {
      return Optional.empty();
    }

    String response = missingFields.get(0);
    if (missingFields.size() == 1) {
      response = response.concat(" is required.");
    } else {
      for (int i = 1; i < missingFields.size(); i++) {
        if (i == (missingFields.size() - 1)) {
          response = response.concat(" and " + missingFields.get(i));
        } else {
          response = response.concat(", " + missingFields.get(i));
        }
      }
      response = response.concat(" are required.");
    }

    return Optional.of(response);
  }

  @Override
  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  private boolean existsByName(String name) {
    return productRepository.existsByName(name);
  }

  @Override
  public ProductListResponseDTO getAvailableProductsInDTO() {
    List<Product> productList = productRepository.findAll();
    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    for (Product product : productList) {
      productDTOs.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDuration(), product.getDescription(), product.getType().getName()));
    }
    return productDTOs;
  }

  @Override
  public Optional<Product> getProductById(Long productId) {
    return productRepository.findById(productId);
  }

  @Override
  public Optional<String> validateProductEditRequestDTO(ProductEditRequestDTO productEditRequestDTO) {
    List<String> missingFields = new ArrayList<>();
    if (productEditRequestDTO.getName() == null || productEditRequestDTO.getName().isBlank()) {
      missingFields.add("Name");
    }
    if (productEditRequestDTO.getPrice() == null) {
      missingFields.add("Price");
    }
    if (productEditRequestDTO.getDuration() == null || productEditRequestDTO.getDuration().isBlank()) {
      missingFields.add("Duration");
    }
    if (productEditRequestDTO.getDescription() == null || productEditRequestDTO.getDescription().isBlank()) {
      missingFields.add("Description");
    }
    if (productEditRequestDTO.getTypeId() == null) {
      missingFields.add("Type id");
    }

    return getErrorMessageByMissingFields(missingFields);
  }

  @Override
  public Product modifyProduct(Product product, ProductEditRequestDTO productEditRequestDTO) throws ProductTypeNotFoundException {
    product.setName(productEditRequestDTO.getName());
    product.setPrice(productEditRequestDTO.getPrice());
    product.setDuration(Integer.parseInt(productEditRequestDTO.getDuration().split(" ")[0]));
    product.setDescription(productEditRequestDTO.getDescription());

    ProductType productType = productTypeService.findById(productEditRequestDTO.getTypeId())
        .orElseThrow(() -> new ProductTypeNotFoundException("ProductType does not exist."));
    product.setType(productType);

    return product;
  }

  @Override
  public void save(Product product) {
    productRepository.save(product);
  }

  @Override
  public ProductEditResponseDTO getProductEditResponseDTO(Product editedProduct) {
    return new ProductEditResponseDTO(editedProduct.getId(), editedProduct.getName(),
        editedProduct.getPrice(), String.valueOf(editedProduct.getDuration()).concat(" hours"),
        editedProduct.getDescription(), editedProduct.getType().getName());
  }

  @Override
  public ProductEditResponseDTO editProduct(Long productId, ProductEditRequestDTO requestDTO)
      throws EmptyFieldsException, ProductNotFoundException, ProductTypeNotFoundException, UniqueNameViolationException {

    Optional<String> errorMessage = validateProductEditRequestDTO(requestDTO);
    if (errorMessage.isPresent()) {
      throw new EmptyFieldsException(errorMessage.get());
    }

    Product productToEdit = findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("Product does not exist."));

    if (!requestDTO.getName().equals(productToEdit.getName())
        && existsByName(requestDTO.getName())) {
      throw new UniqueNameViolationException("ProductName already exists.");
    }

    if (!productToEdit.getType().getId().equals(requestDTO.getTypeId())
        && productTypeService.findById(requestDTO.getTypeId()).isEmpty()) {
      throw new ProductTypeNotFoundException("ProductType does not exist.");
    }

    Product editedProduct = modifyProduct(productToEdit, requestDTO);
    save(editedProduct);
    return getProductEditResponseDTO(editedProduct);
  }
}