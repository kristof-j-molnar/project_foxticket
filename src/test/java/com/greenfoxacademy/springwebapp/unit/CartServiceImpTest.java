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
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.CartService;
import com.greenfoxacademy.springwebapp.services.CartServiceImp;
import com.greenfoxacademy.springwebapp.services.ProductService;
import com.greenfoxacademy.springwebapp.services.ProductServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceImpTest {

  CartService cartService;

  ProductService productService;

  ProductRepository productRepository;

  UserRepository userRepository;

  CartRepository cartRepository;

  @BeforeEach
  void init() {
    userRepository = Mockito.mock(UserRepository.class);
    cartRepository = Mockito.mock(CartRepository.class);
    productRepository = Mockito.mock(ProductRepository.class);
    cartService = new CartServiceImp(cartRepository, userRepository);
    productService = new ProductServiceImp(productRepository);
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

  @Test
  void removeProduct_CartContainsProduct_ProductRemoved() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    Cart cart = user.getCart();
    long id = 1;
    cart.addProduct(p1);
    p1.setId(id);

    Mockito.when(userRepository.findUserByEmail("lacika.com")).thenReturn(Optional.of(user));
    Mockito.when(productService.getProductById(id)).thenReturn(Optional.of(p1));

    cartService.removeProduct(user, p1);

    assertTrue(cart.getProductList().isEmpty());
  }

  @Test
  void removeProduct_CartDoesNotContainProduct_NoChange() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    Product p2 = new Product("Another Product", 100, 20, "Description");

    Cart cart = user.getCart();
    long id1 = 1;
    cart.addProduct(p1);
    p1.setId(id1);
    long id2 = 2;
    p2.setId(id2);

    Mockito.when(userRepository.findUserByEmail("lacika.com")).thenReturn(Optional.of(user));
    Mockito.when(productService.getProductById(id2)).thenReturn(Optional.of(p2));

    cartService.removeProduct(user, p2);

    assertEquals(1, cart.getProductList().size());
    assertTrue(cart.getProductList().contains(p1));
  }

  @Test
  void clearCart_CartNotEmpty_CartCleared() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    Cart cart = user.getCart();
    cart.addProduct(p1);

    Mockito.when(userRepository.findUserByEmail("lacika.com")).thenReturn(Optional.of(user));

    cartService.clearCart(user);

    assertTrue(cart.getProductList().isEmpty());
  }
}