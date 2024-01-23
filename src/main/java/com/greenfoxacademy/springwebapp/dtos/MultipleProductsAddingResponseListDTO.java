package com.greenfoxacademy.springwebapp.dtos;

import java.util.ArrayList;
import java.util.List;

public class MultipleProductsAddingResponseListDTO {
  private List<MultipleProductsAddingResponseItemDTO> items;

  public MultipleProductsAddingResponseListDTO() {
    items = new ArrayList<>();
  }

  public List<MultipleProductsAddingResponseItemDTO> getItems() {
    return items;
  }

  public void add(MultipleProductsAddingResponseItemDTO product) {
    items.add(product);
  }
}
