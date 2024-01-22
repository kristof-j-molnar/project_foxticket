package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  private int price;

  private int duration;

  private String description;

  private boolean isDeleted;

  @ManyToOne
  private ProductType type;

  public Product() {
  }

  public Product(String name, int price, int duration, String description, boolean isDeleted) {
    this.name = name;
    this.price = price;
    this.duration = duration;
    this.description = description;
    this.isDeleted = isDeleted;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductType getType() {
    return type;
  }

  public void setType(ProductType type) {
    this.type = type;
  }
}
