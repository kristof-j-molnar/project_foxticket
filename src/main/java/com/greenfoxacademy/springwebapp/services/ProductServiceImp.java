package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductEditResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Override
  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public Boolean existsByName(String name) {
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
  public String validateProductEditDTO(ProductEditRequestDTO productEditRequestDTO) {
    if (productEditRequestDTO.getName() == null || productEditRequestDTO.getName().isBlank()) {
      return "Name";
    }
    if (productEditRequestDTO.getPrice() == null) {
      return "Price";
    }
    if (productEditRequestDTO.getDuration() == null || productEditRequestDTO.getDuration().isBlank()) {
      return "Duration";
    }
    if (productEditRequestDTO.getDescription() == null || productEditRequestDTO.getDescription().isBlank()) {
      return "Description";
    }
    if (productEditRequestDTO.getTypeId() == null) {
      return "Type id";
    }

    return null;
  }

  @Override
  public Product editProduct(Product product, ProductEditRequestDTO productEditRequestDTO) throws IllegalArgumentException {
    product.setName(productEditRequestDTO.getName());
    product.setPrice(productEditRequestDTO.getPrice());
    product.setDuration(Integer.parseInt(productEditRequestDTO.getDuration().split(" ")[0]));
    product.setDescription(productEditRequestDTO.getDescription());
    if (product.getType().getId() != productEditRequestDTO.getTypeId()) {
      Optional<ProductType> productTypeOptional = productTypeService.findById(productEditRequestDTO.getTypeId());
      if (productTypeOptional.isPresent()) {
        product.setType(productTypeOptional.get());
      } else {
        throw new IllegalArgumentException("ProductType does not exist.");
      }
    }

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
}