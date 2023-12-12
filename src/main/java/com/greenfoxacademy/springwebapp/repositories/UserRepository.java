package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  Optional<User> findUserByEmail(String email);
}
