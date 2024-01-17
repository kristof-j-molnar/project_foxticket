package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfigurer {

  private JwtRequestFilter jwtRequestFilter;

  @Autowired
  public SecurityConfigurer(JwtRequestFilter jwtRequestFilter) {
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/users/**", "/api/news", "/api/products", "/api/cart", "/test"));

    http.authorizeHttpRequests(authorize -> authorize

        .requestMatchers("/admin").hasRole("ADMIN")
            .requestMatchers("/api/users/**", "/api/news")
            .permitAll()
            .anyRequest()
            .authenticated())
        .httpBasic(basic -> basic.authenticationEntryPoint(((request, response, authException) -> response.sendError(HttpStatus.UNAUTHORIZED.value(), "no-no"))));

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}