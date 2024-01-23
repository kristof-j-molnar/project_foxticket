package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartItemRepository;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImp implements CartService {

  private final CartRepository cartRepository;
  private final UserRepository userRepository;

  private ProductRepository productRepository;

  private CartItemRepository cartItemRepository;

  @Autowired
  public CartServiceImp(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
    this.cartRepository = cartRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.cartItemRepository = cartItemRepository;
  }

  @Transactional
  public CartDTO getProductsInCartDTO(Integer id) {
    User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User is not found"));
    return mapToCartDTO(user.getCart());
  }

  private CartDTO mapToCartDTO(Cart cart) {
    CartDTO cartDto = new CartDTO();
    if (cart != null || !cart.getProductList().isEmpty()) {
      List<Product> productList = cart.getProductList();
      productList.stream().map(CartItemDTO::new).forEach(cartDto::add);
    }
    return cartDto;
  }

  public void save(Cart cart) {
    cartRepository.save(cart);
  }

  @Transactional
  public ProductAddingResponseDTO addProduct(User user, ProductAddingRequestDTO productDTO) {
    Product product = getProductById(productDTO.getProductId());
    Cart cart = user.getCart();
    cart.addProduct(product);
    cartRepository.save(cart);
    int amount = getAmount(cart, product);
    return new ProductAddingResponseDTO(cart.getId(), product.getId(), amount);
  }

  @Transactional
  public MultipleProductsAddingResponseListDTO addMultipleProduct(User user, ProductAddingRequestDTO productDTO) {
    Cart cart = user.getCart();
    Product product = getProductById(productDTO.getProductId());
    MultipleProductsAddingResponseListDTO itemsDTO = new MultipleProductsAddingResponseListDTO();
    for (int i = 0; i < productDTO.getAmount(); i++) {
      cart.addProduct(product);
      cartRepository.save(cart);
      itemsDTO.add(new MultipleProductsAddingResponseItemDTO((long) cart.getCartItems().size(), product.getId()));
    }
    return itemsDTO;
  }

  private Product getProductById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product is not found"));
  }

  public boolean isEmptyAddRequest(ProductAddingRequestDTO productDTO) {
    return productDTO == null || productDTO.getProductId() == null;
  }

  private int getAmount(Cart cart, Product product) {
    int count = 0;
    for (Product p : cart.getProductList()) {
      if (p.getId().equals(product.getId())) {
        count++;
      }
    }
    return count;
  }
}
