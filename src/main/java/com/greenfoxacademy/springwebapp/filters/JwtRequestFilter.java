package com.greenfoxacademy.springwebapp.filters;

import com.greenfoxacademy.springwebapp.dtos.MyUserDetails;
import com.greenfoxacademy.springwebapp.services.MyUserDetailsService;
import com.greenfoxacademy.springwebapp.utilities.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  private JwtUtil jwtUtil;
  private MyUserDetailsService myUserDetailsService;

  @Autowired
  public JwtRequestFilter(JwtUtil jwtUtil, MyUserDetailsService myUserDetailsService) {
    this.jwtUtil = jwtUtil;
    this.myUserDetailsService = myUserDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");
    Integer userId = null;
    String jwt = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      Claims claims = jwtUtil.extractAllClaims(jwt);
      userId = (Integer) claims.get("userId");
    }
    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      MyUserDetails userDetails = this.myUserDetailsService.loadUserById(userId);
      if (jwtUtil.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    chain.doFilter(request, response);
  }
}