package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
