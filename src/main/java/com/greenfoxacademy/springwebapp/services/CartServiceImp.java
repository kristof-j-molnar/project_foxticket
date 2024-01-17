package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    int count = 0;
    for (int i = 0; i < cart.getProductList().size(); i++) {
      if (cart.getProductList().get(i).getId().equals(product.getId())) {
        count++;
      }
    }
    return count;
  }

  public boolean isEmptyDTO(ProductAddingRequestDTO product) {
    if (product == null || product.getProductId() == null) {
      return true;
    }
    return false;
  }

  private CartDTO mapToCartDTO(Cart cart) {
    List<Product> productList = cart.getProductList();
    CartDTO cartDto = new CartDTO();
    productList.forEach(product -> cartDto.add(new CartItemDTO(product.getId(), product.getName(), product.getPrice())));
    return cartDto;
  }
}
