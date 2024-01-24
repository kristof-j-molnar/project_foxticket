package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductAddingResponseDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.CartService;
import com.greenfoxacademy.springwebapp.services.CartServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceImpTest {

  CartService cartService;

  UserRepository userRepository;

  CartRepository cartRepository;

  @BeforeEach
  void init() {
    userRepository = Mockito.mock(UserRepository.class);
    cartRepository = Mockito.mock(CartRepository.class);
    cartService = new CartServiceImp(cartRepository, userRepository);
  }

  @Test
  void getProductInCart_ReturnCartDTO() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = user.getCart();
    cart.addProduct(p1);
    int id = 1;
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

    CartDTO cartDTO = new CartDTO();
    cartDTO.add(new CartItemDTO(1L, "Vonaljegy", 480));

    CartDTO actual = cartService.getProductsInCartDTO(id);
    assertEquals(cartDTO.getCart().get(0).getName(), actual.getCart().get(0).getName());
  }

  @Test
  void addProductInCart_ReturnResponseDTO() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = user.getCart();
    int id = 1;
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    p1.setId((long) id);

    ProductAddingResponseDTO expected = new ProductAddingResponseDTO(1L, 1L, 1);

    ProductAddingResponseDTO actual = cartService.addProduct(user, p1);
    assertEquals(expected.getAmount(), actual.getAmount());
  }

  @Test
  void isEmptyAddRequest_ReturnTrue() {
    ProductAddingRequestDTO product = new ProductAddingRequestDTO(null);
    boolean actual = cartService.isEmptyAddRequest(product);
    assertTrue(actual);
  }

  @Test
  void isEmptyAddRequest_ReturnTrue2() {
    boolean actual = cartService.isEmptyAddRequest(null);
    assertTrue(actual);
  }

  @Test
  void isEmptyAddRequest_ReturnFalse() {
    ProductAddingRequestDTO product = new ProductAddingRequestDTO(1L);
    boolean actual = cartService.isEmptyAddRequest(product);
    assertFalse(actual);
  }
}
