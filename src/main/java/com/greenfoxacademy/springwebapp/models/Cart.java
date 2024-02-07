package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "carts")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "cart", fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<CartItem> cartItems;

  public Cart() {
    cartItems = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Product> getProductList() {
    return cartItems.stream().map(CartItem::getProduct).toList();
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  public void addProduct(Product product) {
    cartItems.add(new CartItem(product, this));
  }

  public void removeProductOnce(Product product) {
    var it = cartItems.iterator();
    while (it.hasNext()) {
      var item = it.next();
      if (item.getProduct().equals(product)) {
        it.remove();
        return;
      }
    }
    throw new EntityNotFoundException("Product not found in the cart");
  }

  public boolean isEmpty() {
    return cartItems.isEmpty();
  }

  public void clear() {
    cartItems.clear();
  }
}