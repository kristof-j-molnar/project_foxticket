package com.greenfoxacademy.springwebapp.filters;

import com.greenfoxacademy.springwebapp.dtos.SecurityUser;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.MyUserDetailsService;
import com.greenfoxacademy.springwebapp.utilities.JwtValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtValidator jwtValidator;
  private final MyUserDetailsService userDetailsService;

  @Autowired
  public JwtRequestFilter(JwtValidator jwtValidator, MyUserDetailsService userDetailsService) {
    this.jwtValidator = jwtValidator;
    this.userDetailsService = userDetailsService;
  }

  private static String getJwtFromHeader(HttpServletRequest request) {
    final String authorizationHeader = request.getHeader("Authorization");
    String jwt = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
    }
    return jwt;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return SecurityContextHolder.getContext().getAuthentication() != null;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

    String jwt = getJwtFromHeader(request);

    if (StringUtils.isBlank(jwt)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      SecurityUser userDetails = getUserDetailsFromJwt(jwt);
      setUserAuthenticated(request, userDetails);
      chain.doFilter(request, response);
    } catch (JwtException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("{ \"message\": \"Invalid token\"}");
    }
  }

  private void setUserAuthenticated(HttpServletRequest request, SecurityUser userDetails) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }

  private SecurityUser getUserDetailsFromJwt(String jwt) {
    Claims claims = jwtValidator.parseAndValidateJwtToken(jwt);

    final Integer userId = ((Integer) claims.get("userId"));
    final boolean isAdmin = (boolean) claims.get("isAdmin");
    final boolean isVerified = (boolean) claims.get("isVerified");

    User user = userDetailsService.loadUserById(userId);
    return new SecurityUser(user, isAdmin, isVerified);
  }
}