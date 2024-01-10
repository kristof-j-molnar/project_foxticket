package com.greenfoxacademy.springwebapp.utilities;

import com.greenfoxacademy.springwebapp.dtos.MyUserDetailsDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
  private final Key SECRET_KEY;

  public JwtUtil(@Value("${JWT_SECRET_KEY}") String encodedSecretKey) {
    SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(encodedSecretKey));
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  public String generateToken(MyUserDetailsDTO userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userDetails.getUserId());
    claims.put("isVerified", userDetails.isVerified());
    claims.put("isAdmin", userDetails.isAdmin());
    return createToken(claims);
  }

  private String createToken(Map<String, Object> claims) {
    return Jwts.builder().setClaims(claims)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS256).compact();
  }

  public Boolean validateToken(String token, MyUserDetailsDTO userDetails) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return (userDetails.getUserId().equals(claims.get("userId"))
        && userDetails.isAdmin().equals(claims.get("isAdmin"))
        && userDetails.isVerified().equals(claims.get("isVerified")));
  }
}