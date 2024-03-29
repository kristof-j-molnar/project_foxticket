package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

@Entity(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  @Column(unique = true)
  private String email;
  private String password;
  private String role;
  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  public User(String name, String email, String password, String role) {
    this();
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public User(User user) {
    this();
    this.name = user.getName();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.role = user.getRole();
  }

  public User() {
    setCart(new Cart());
    cart.setUser(this);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }
}