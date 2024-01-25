package com.greenfoxacademy.springwebapp.dtos;

import java.util.ArrayList;
import java.util.List;

public class MultipleProductsAddingResponseListDTO {
  private List<ProductAddingResponseItemDTO> items;

  public MultipleProductsAddingResponseListDTO() {
    items = new ArrayList<>();
  }

  public List<ProductAddingResponseItemDTO> getItems() {
    return items;
  }

  public void add(ProductAddingResponseItemDTO product) {
    items.add(product);
  }
}
