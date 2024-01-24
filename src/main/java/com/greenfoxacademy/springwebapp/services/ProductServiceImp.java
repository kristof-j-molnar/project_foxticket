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
import java.util.NoSuchElementException;
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

  private static Optional<String> getErrorMessageByMissingFields(List<String> missingFields) {
    final int size = missingFields.size();
    return switch (size) {
      case 0 -> Optional.empty();
      case 1 -> Optional.of(String.format("%s is required.", missingFields.get(0)));
      default -> Optional.of(String.format("%s and %s are required.",
          String.join(", ", missingFields.subList(0, size - 1)),
          missingFields.get(size - 1)));
    };
  }

  private static ProductEditResponseDTO getProductEditResponseDTO(Product editedProduct) {
    return new ProductEditResponseDTO(editedProduct.getId(), editedProduct.getName(),
        editedProduct.getPrice(), String.valueOf(editedProduct.getDuration()).concat(" hours"),
        editedProduct.getDescription(), editedProduct.getType().getName());
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
      productDTOs.add(new ProductDTO(product));
    }
    return productDTOs;
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

  private Product modifyProduct(Product product, ProductEditRequestDTO productEditRequestDTO) {
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
  public ProductEditResponseDTO editProduct(Long productId, ProductEditRequestDTO requestDTO) {

    validateProductEditRequestDTO(requestDTO).ifPresent(message -> {
      throw new EmptyFieldsException(message);
    });

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

  @Override
  public Product deleteProductById(Long productId) {
    Product productToDelete = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("The product does not exist!"));

    if (productToDelete.isDeleted()) {
      return productToDelete;
    }

    productToDelete.setDeleted(true);
    productRepository.save(productToDelete);
    return productToDelete;
  }
}


