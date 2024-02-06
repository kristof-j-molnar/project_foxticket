package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.*;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.CartService;
import com.greenfoxacademy.springwebapp.services.CartServiceImp;
import com.greenfoxacademy.springwebapp.services.UserAuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CartServiceImpTest {

  CartService cartService;

  UserRepository userRepository;

  CartRepository cartRepository;

  UserAuthenticationService authenticationService;

  ProductRepository productRepository;

  @BeforeEach
  void init() {
    userRepository = Mockito.mock(UserRepository.class);
    cartRepository = Mockito.mock(CartRepository.class);
    productRepository = Mockito.mock(ProductRepository.class);
    authenticationService = Mockito.mock(UserAuthenticationService.class);
    cartService = new CartServiceImp(cartRepository, userRepository, authenticationService, productRepository);
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

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(productRepository.findById((long) id)).thenReturn(Optional.of(p1));
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    Mockito.when(authenticationService.getCurrentUserEmail(authentication)).thenReturn("lacika.com");
    Mockito.when(userRepository.findUserByEmail("lacika.com")).thenReturn(Optional.of(user));

    p1.setId((long) id);
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(1L, 1);
    MultipleProductsAddingResponseListDTO expect = new MultipleProductsAddingResponseListDTO();
    expect.add(new ProductAddingResponseItemDTO(1L, 1L));

    MultipleProductsAddingResponseListDTO actual = cartService.addProduct(request);
    verify(cartRepository).save(cart);
  }

  @Test
  void addProductInCart_withEmptyProductAmount_ReturnResponseDTO() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = user.getCart();
    int id = 1;

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(productRepository.findById((long) id)).thenReturn(Optional.of(p1));
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    Mockito.when(authenticationService.getCurrentUserEmail(authentication)).thenReturn("lacika.com");
    Mockito.when(userRepository.findUserByEmail("lacika.com")).thenReturn(Optional.of(user));

    p1.setId((long) id);
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(1L);
    ProductAddingResponseItemDTO expected = new ProductAddingResponseItemDTO(1L, 1L);
    MultipleProductsAddingResponseListDTO actual = cartService.addProduct(request);
    verify(cartRepository).save(cart);
  }

  @Test
  void addMultipleProductInCart_ReturnResponseDTO() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = user.getCart();
    int id = 1;

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(productRepository.findById((long) id)).thenReturn(Optional.of(p1));
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    Mockito.when(authenticationService.getCurrentUserEmail(authentication)).thenReturn("lacika.com");
    Mockito.when(userRepository.findUserByEmail("lacika.com")).thenReturn(Optional.of(user));

    p1.setId((long) id);
    ProductAddingRequestDTO request = new ProductAddingRequestDTO(1L, 2);
    MultipleProductsAddingResponseListDTO expected = new MultipleProductsAddingResponseListDTO();
    ProductAddingResponseItemDTO item1 = new ProductAddingResponseItemDTO(1L, 1L);
    ProductAddingResponseItemDTO item2 = new ProductAddingResponseItemDTO(2L, 1L);
    expected.add(item1);
    expected.add(item2);

    MultipleProductsAddingResponseListDTO actual = cartService.addProduct(request);
    verify(cartRepository, times(2)).save(cart);
  }

  @Test
  void addProduct_withNotFoundProduct_returnError() {
    User user = new User("user", "lacika.com", "pass", "User");
    Cart cart = user.getCart();
    int id = 1;

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(cartRepository.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    Mockito.when(authenticationService.getCurrentUserEmail(authentication)).thenReturn("lacika.com");
    Mockito.when(userRepository.findUserByEmail("lacika.com")).thenReturn(Optional.of(user));

    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
        () -> cartService.addProduct(new ProductAddingRequestDTO(10L, 2)));

    Assertions.assertEquals("Product is not found", exception.getMessage());
  }

  @Test
  void removeProduct_CartContainsProduct_ProductRemoved() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    Cart cart = user.getCart();
    long id = 1;
    long cartItemId = 1;
    p1.setId(id);
    cart.addProduct(p1);

    Authentication auth = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
    Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(p1));
    Mockito.when(authenticationService.getCurrentUserEmail(auth)).thenReturn(user.getEmail());
    SecurityContextHolder.setContext(securityContext);

    cartService.removeProduct(id, auth);
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

    Authentication auth = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
    Mockito.when(productRepository.findById(id2)).thenReturn(Optional.of(p2));
    Mockito.when(authenticationService.getCurrentUserEmail(auth)).thenReturn(user.getEmail());
    SecurityContextHolder.setContext(securityContext);

    assertThrows(EntityNotFoundException.class, () -> cartService.removeProduct(id2, auth));
    assertEquals(1, cart.getProductList().size());
    assertTrue(cart.getProductList().contains(p1));
  }

  @Test
  void clearCart_CartNotEmpty_CartCleared() {
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    Cart cart = user.getCart();
    cart.addProduct(p1);
    long id = 1;

    Authentication auth = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
    Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(p1));
    Mockito.when(authenticationService.getCurrentUserEmail(auth)).thenReturn(user.getEmail());
    SecurityContextHolder.setContext(securityContext);

    cartService.clearCart(auth);

    assertTrue(cart.getProductList().isEmpty());
  }
}