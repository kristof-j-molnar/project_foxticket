package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {

  private CartRepository cartRepository;

  @Autowired
  public CartServiceImp(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  public CartDTO getProductsInCartDTO(Integer id) {
    Optional<Cart> cart = cartRepository.findByUserId(id);
    if (cart.isPresent()) {
      List<Product> productList = cart.get().getProductList();
      CartDTO cartDto = new CartDTO();
      for (Product product : productList) {
        cartDto.add(new CartItemDTO(product.getId(), product.getName(), product.getPrice()));
      }
      return cartDto;
    }
    return null;
  }
}
