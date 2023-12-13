package com.greenfoxacademy.springwebapp.utilities;

import com.greenfoxacademy.springwebapp.dtos.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
  private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  public String generateToken(MyUserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userDetails.getUserId());
    claims.put("isVerified", userDetails.getIsVerified());
    claims.put("isAdmin", userDetails.getIsAdmin());
    return createToken(claims);
  }

  private String createToken(Map<String, Object> claims) {
    return Jwts.builder().setClaims(claims)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS256).compact();
  }

  public Boolean validateToken(String token, MyUserDetails userDetails) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return (userDetails.getUserId().equals(claims.get("userId"))
        && userDetails.getIsAdmin().equals(claims.get("isAdmin"))
        && userDetails.getIsVerified().equals(claims.get("isVerified")));
  }
}