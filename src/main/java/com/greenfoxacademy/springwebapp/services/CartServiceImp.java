package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImp implements CartService {

  private final CartRepository cartRepository;
  private final UserRepository userRepository;

  private final UserAuthenticationService authenticationService;

  private final ProductRepository productRepository;

  @Autowired
  public CartServiceImp(CartRepository cartRepository, UserRepository userRepository, UserAuthenticationService authenticationService, ProductRepository productRepository) {
    this.cartRepository = cartRepository;
    this.userRepository = userRepository;
    this.authenticationService = authenticationService;
    this.productRepository = productRepository;
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

  public void save(Cart cart) {
    cartRepository.save(cart);
  }

  @Transactional
  public MultipleProductsAddingResponseListDTO addProduct(ProductAddingRequestDTO productDTO) {
    User user = getCurrentUser();
    Product product = productRepository.findById(productDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product is not found"));
    Cart cart = user.getCart();
    MultipleProductsAddingResponseListDTO itemsDTO = new MultipleProductsAddingResponseListDTO();

    if (productDTO.getAmount() == null) {
      productDTO.setAmount(1);
    }
    for (int i = 0; i < productDTO.getAmount(); i++) {
      cart.addProduct(product);
      cartRepository.save(cart);
      itemsDTO.add(new ProductAddingResponseItemDTO(cart.getCartItems().get(cart.getCartItems().size() - 1).getId(), product.getId()));
    }

    return itemsDTO;
  }

  private User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userRepository.findUserByEmail(authenticationService.getCurrentUserEmail(authentication)).orElseThrow(() -> new EntityNotFoundException("User is not found"));
  }

  public boolean isEmptyAddRequest(ProductAddingRequestDTO productDTO) {
    return productDTO == null || productDTO.getProductId() == null;
  }

  @Override
  public void removeProduct(Long itemId, Authentication auth) {
    User user = userRepository.findUserByEmail(authenticationService.getCurrentUserEmail(auth))
        .orElseThrow(() -> new EntityNotFoundException("User is invalid"));
    Cart cart = user.getCart();
    Product productToRemove = productRepository.findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("No such product with the given ID"));
    cart.removeProduct(productToRemove);
    cartRepository.save(cart);
  }

  @Override
  public void clearCart(Authentication auth) {
    User user = userRepository.findUserByEmail(authenticationService.getCurrentUserEmail(auth))
        .orElseThrow(() -> new EntityNotFoundException("User is invalid"));
    Cart cart = user.getCart();
    if (cart.getProductList().isEmpty()) {
      return;
    }
    cart.getProductList().clear();
    cartRepository.save(cart);
  }
}