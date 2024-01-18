package com.greenfoxacademy.springwebapp.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
public class JwtValidator {
  private final Key SECRET_KEY;

  public JwtValidator(@Value("${JWT_SECRET_KEY}") String encodedSecretKey) {
    SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(encodedSecretKey));
  }

  public Claims parseAndValidateJwtToken(String jwt) {
    return Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(jwt)
        .getBody();
  }
}