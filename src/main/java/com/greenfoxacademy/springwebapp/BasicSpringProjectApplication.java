package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicSpringProjectApplication implements CommandLineRunner {
  private UserRepository userRepository;

  @Autowired
  public BasicSpringProjectApplication(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(BasicSpringProjectApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
<<<<<<< HEAD
    User user1 = new User("reka", "ferenczy.reka01@gmail.com", "reka12345", "USER");
    userRepository.save(user1);

    User user = new User("user","lacika.com","pass","User");
=======
    User user = new User("user", "lacika.com", "pass", "User");
>>>>>>> 524ae6f (fix the indentations)
    userRepository.save(user);
  }
}
