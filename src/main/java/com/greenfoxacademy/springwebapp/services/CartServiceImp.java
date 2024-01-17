package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {

  private CartRepository cartRepository;
  private UserRepository userRepository;

  @Autowired
  public CartServiceImp(CartRepository cartRepository, UserRepository userRepository) {
    this.cartRepository = cartRepository;
    this.userRepository = userRepository;
  }

  public CartDTO getProductsInCartDTO(Integer id) {
    Optional<User> optUser = userRepository.findById(id);
    if (optUser.isPresent() && (optUser.get().getCart() == null || optUser.get().getCart().getProductList().isEmpty())) {
      return new CartDTO();
    }
    return cartRepository.findByUserId(id)
        .map(this::mapToCartDTO)
        .orElseThrow(() -> new EntityNotFoundException("User is not found"));
  }

  public void save(Cart cart) {
    cartRepository.save(cart);
  }

  public int getAmount(Cart cart, Product product) {
    return (int) cart.getProductList().stream()
        .filter(cartItem -> cartItem.getId().equals(product.getId()))
        .count();
  }

  private CartDTO mapToCartDTO(Cart cart) {
    List<Product> productList = cart.getProductList();
    CartDTO cartDto = new CartDTO();
    productList.forEach(product -> cartDto.add(new CartItemDTO(product.getId(), product.getName(), product.getPrice())));
    return cartDto;
  }
}
