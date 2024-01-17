package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.CartServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceImpTest {

  @Test
  void getProductInCart_ReturnCartDTOAnd200() {
    var repo1 = Mockito.mock(CartRepository.class);
    var repo2 = Mockito.mock(UserRepository.class);
    User user = new User("user", "lacika.com", "pass", "User");
    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    Cart cart = new Cart(user);
    cart.addProduct(p1);
    int id = 1;
    Mockito.when(repo1.findByUserId(id)).thenReturn(Optional.of(cart));
    Mockito.when(repo2.findById(id)).thenReturn(Optional.of(user));

    CartServiceImp cartService = new CartServiceImp(repo1, repo2);
    CartDTO cartDTO = new CartDTO();
    cartDTO.add(new CartItemDTO(1L, "Vonaljegy", 480));

    CartDTO actual = cartService.getProductsInCartDTO(id);
    assertEquals(cartDTO.getCart().get(0).getName(), actual.getCart().get(0).getName());
  }
}
