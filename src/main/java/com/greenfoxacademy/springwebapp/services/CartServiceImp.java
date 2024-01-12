package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartDTO;
import com.greenfoxacademy.springwebapp.dtos.CartItemDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import jakarta.persistence.EntityNotFoundException;
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
    return cartRepository.findByUserId(id)
        .map(this::mapToCartDTO)
        .orElseThrow(() -> new EntityNotFoundException(("User is not found")));
  }

  private CartDTO mapToCartDTO(Cart cart) {
    List<Product> productList = cart.getProductList();
    CartDTO cartDto = new CartDTO();
    productList.forEach(product ->
        cartDto.add(new CartItemDTO(product.getId(), product.getName(), product.getPrice())));
    return cartDto;
  }
}
