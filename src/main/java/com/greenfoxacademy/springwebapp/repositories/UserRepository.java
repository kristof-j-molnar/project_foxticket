package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
<<<<<<< HEAD
=======

>>>>>>> 524ae6f (fix the indentations)
  Optional<User> findUserByEmail(String email);
}
