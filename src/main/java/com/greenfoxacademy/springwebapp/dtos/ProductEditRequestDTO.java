package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductEditRequestDTO {
  private String name;
  private Integer price;
  private String duration;
  private String description;
  @JsonProperty("type_id")
  private Long typeId;

  public ProductEditRequestDTO() {
  }

  public ProductEditRequestDTO(String name, int price, String duration, String description, Long typeId) {
    this.name = name;
    this.price = price;
    this.duration = duration;
    this.description = description;
    this.typeId = typeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getTypeId() {
    return typeId;
  }

  public void setTypeId(Long typeId) {
    this.typeId = typeId;
  }
}