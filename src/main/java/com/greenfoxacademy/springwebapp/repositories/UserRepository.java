package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  Optional<User> findUserByEmail(String email);

}
