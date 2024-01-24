package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingResponseDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
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

  @Autowired
  public CartServiceImp(CartRepository cartRepository, UserRepository userRepository) {
    this.cartRepository = cartRepository;
    this.userRepository = userRepository;
  }

  @Override
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

  @Override
  public boolean isEmptyAddRequest(ProductAddingRequestDTO product) {
    return product == null || product.getProductId() == null;
  }

  @Override
  public ProductAddingResponseDTO addProduct(User user, Product product) {
    Cart cart = user.getCart();
    cart.addProduct(product);
    cartRepository.save(cart);
    int amount = getAmount(cart, product);
    return new ProductAddingResponseDTO(cart.getId(), product.getId(), amount);
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