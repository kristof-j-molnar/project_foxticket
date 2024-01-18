package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

  @Query(value = "SELECT carts.id  FROM carts JOIN users ON carts.id = cart_id WHERE users.id = :id", nativeQuery = true)
  Optional<Cart> findByUserId(Integer id);
}

