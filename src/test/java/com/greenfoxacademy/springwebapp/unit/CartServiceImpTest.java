package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartItemRepository;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
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

  ProductRepository productRepository;

  CartItemRepository cartItemRepository;

  @BeforeEach
  void init() {
    userRepository = Mockito.mock(UserRepository.class);
    cartRepository = Mockito.mock(CartRepository.class);
    productRepository = Mockito.mock(ProductRepository.class);
    cartItemRepository = Mockito.mock(CartItemRepository.class);
    cartService = new CartServiceImp(cartRepository, userRepository, productRepository,cartItemRepository);
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
  void addProductInCart_withOneProduct_ReturnResponseDTO() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = user.getCart();
    int id = 1;
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(productRepository.findById((long) id)).thenReturn(Optional.of(p1));

    p1.setId((long) id);
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(1L, 1);
    ProductAddingResponseDTO expected = new ProductAddingResponseDTO(1L, 1L, 1);

    ProductAddingResponseDTO actual = cartService.addProduct(user, request);
    assertEquals(expected.getAmount(), actual.getAmount());
  }

  @Test
  void addProductInCart_withEmptyProductAmount_ReturnResponseDTO() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = user.getCart();
    int id = 1;
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(productRepository.findById((long) id)).thenReturn(Optional.of(p1));

    p1.setId((long) id);
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(1L, 2);
    ProductAddingResponseDTO expected = new ProductAddingResponseDTO(1L, 1L, 1);

    ProductAddingResponseDTO actual = cartService.addProduct(user, request);
    assertEquals(expected.getAmount(), actual.getAmount());
  }

  @Test
  void addMultipleProductInCart_ReturnResponseDTO() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = user.getCart();
    int id = 1;
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(productRepository.findById((long) id)).thenReturn(Optional.of(p1));

    p1.setId((long) id);
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(1L, 2);
    MultipleProductsAddingResponseListDTO expected = new MultipleProductsAddingResponseListDTO();
    MultipleProductsAddingResponseItemDTO item1 = new MultipleProductsAddingResponseItemDTO(1L, 1L);
    MultipleProductsAddingResponseItemDTO item2 = new MultipleProductsAddingResponseItemDTO(2L, 1L);
    expected.add(item1);
    expected.add(item2);

    MultipleProductsAddingResponseListDTO actual = cartService.addMultipleProduct(user, request);
    assertEquals(expected.getItems().size(), actual.getItems().size());
    assertEquals(expected.getItems().get(0).getProductId(), actual.getItems().get(0).getProductId());
  }
}
