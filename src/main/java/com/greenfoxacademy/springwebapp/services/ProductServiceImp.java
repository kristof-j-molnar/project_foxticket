package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListResponseDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfoxacademy.springwebapp.dtos.CartDTO;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

  private ProductRepository productRepository;
  private CartRepository cartRepository;

  @Autowired
  public ProductServiceImp(ProductRepository productRepository, CartRepository cartRepository) {
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
  }

  public ProductListResponseDTO getAvailableProductsInDTO() {
    List<Product> productList = productRepository.findAll();
    ProductListResponseDTO productDTOs = new ProductListResponseDTO();
    for (Product product : productList) {
      productDTOs.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDuration(), product.getDescription(), product.getType().getName()));
    }
    return productDTOs;
  }

  public CartDTO getProductsInCartDTO(Integer id) {
    Optional<Cart> cart = cartRepository.findByUserId(id);
    if (cart.isPresent()) {
      List<Product> productList = productRepository.findProductsByCart(cart);
      CartDTO cartDto = new CartDTO();
      for (Product product : productList) {
        cartDto.add(new CartItemDTO(product.getId(), product.getName(), product.getPrice()));
      }
      return cartDto;
    }
    return null;
  }
}
