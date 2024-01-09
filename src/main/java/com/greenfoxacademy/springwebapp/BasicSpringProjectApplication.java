package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BasicSpringProjectApplication implements CommandLineRunner {
  private UserRepository userRepository;
  private ProductTypeRepository productTypeRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public BasicSpringProjectApplication(UserRepository userRepository, ProductTypeRepository productTypeRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.productTypeRepository = productTypeRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public static void main(String[] args) {
    SpringApplication.run(BasicSpringProjectApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    User user1 = new User("reka", "ferenczy.reka01@gmail.com", bCryptPasswordEncoder.encode("reka12345"), "USER");
    userRepository.save(user1);
    User user = new User("user", "lacika.com", bCryptPasswordEncoder.encode( "pass"), "User");
    userRepository.save(user);

    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    Product p2 = new Product("Vonaljegy", 360, 90, "90 perces vonaljegy BP agglomerációjában!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    t1.addProduct(p2);
    productTypeRepository.save(t1);
  }
}