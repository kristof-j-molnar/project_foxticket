package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.CartEmptyException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImp implements CartService {

  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final UserAuthenticationService userAuthenticationService;

  @Autowired
  public CartServiceImp(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, UserAuthenticationService userAuthenticationService) {
    this.cartRepository = cartRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.userAuthenticationService = userAuthenticationService;
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

  @Override
  public void removeProduct(Long itemId, Authentication auth) {
    User user = userRepository.findUserByEmail(userAuthenticationService.getCurrentUserEmail(auth))
        .orElseThrow(() -> new EntityNotFoundException("User is invalid"));

    Cart cart = user.getCart();
    if (cart == null || cart.getProductList().isEmpty()) {
      throw new CartEmptyException("The user's cart is already empty");
    }
    Product productToRemove = cart.getProductFromCart(cart, itemId);
    if (productToRemove == null) {
      throw new EntityNotFoundException("Product not found in the cart");
    }

    cart.removeProduct(productToRemove);
    cartRepository.save(cart);
  }

  @Override
  public void clearCart(Authentication auth) {
    User user = userRepository.findUserByEmail(userAuthenticationService.getCurrentUserEmail(auth))
        .orElseThrow(() -> new EntityNotFoundException("User is invalid"));
    Cart cart = user.getCart();
    if (cart == null || cart.getProductList().isEmpty()) {
      throw new CartEmptyException("The user's cart is already empty");
    }
    cart.getProductList().clear();
    cartRepository.save(cart);
  }
}