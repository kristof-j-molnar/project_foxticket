package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.Product;

public class ProductDTO {

  private Long id;

  private String name;

  private int price;

  private int duration;

  private String description;

  private String type;

  private boolean isDeleted;

  public ProductDTO() {
  }

  public ProductDTO(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.price = product.getPrice();
    this.duration = product.getDuration();
    this.description = product.getDescription();
    this.type = String.valueOf(product.getType());
    this.isDeleted = product.isDeleted();
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
