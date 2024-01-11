package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BasicSpringProjectApplication implements CommandLineRunner {
  private UserRepository userRepository;
  private ProductTypeRepository productTypeRepository;
  private CartRepository cartRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public BasicSpringProjectApplication(UserRepository userRepository, ProductTypeRepository productTypeRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
    this.userRepository = userRepository;
    this.productTypeRepository = productTypeRepository;
    this.passwordEncoder = passwordEncoder;
    this.cartRepository = cartRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(BasicSpringProjectApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    User user1 = new User("reka", "ferenczy.reka01@gmail.com", passwordEncoder.encode("reka12345"), "USER");
    userRepository.save(user1);
    User user = new User("user", "lacika.com", passwordEncoder.encode("pass"), "User");
    userRepository.save(user);

    Product p1 = new Product("Vonaljegy", 480, 90, "90 perces vonaljegy BP-n!");
    Product p2 = new Product("Vonaljegy", 360, 90, "90 perces vonaljegy BP agglomerációjában!");
    ProductType t1 = new ProductType("Jegy");
    t1.addProduct(p1);
    t1.addProduct(p2);
    productTypeRepository.save(t1);

    Cart cart1 = new Cart(user);
    cartRepository.save(cart1);
    cart1.addProduct(p1);
    cart1.addProduct(p2);
    cartRepository.save(cart1);
  }
}