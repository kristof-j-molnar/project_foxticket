package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {
  private Boolean isAdmin;
  private Boolean isVerified;
  private List<GrantedAuthority> roles;

  private User user;

  public SecurityUser() {
  }

  public SecurityUser(User user) {
    this.user = user;
    isVerified = true;
    isAdmin = false;
    roles = Arrays.stream(user
            .getRole()
            .split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  public SecurityUser(User user, boolean isAdmin, boolean isVerified) {
    this(user);
    this.isAdmin = isAdmin;
    this.isVerified = isVerified;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Integer getUserId() {
    return user.getId();
  }

  public Boolean isAdmin() {
    return isAdmin;
  }

  public Boolean isVerified() {
    return isVerified;
  }

  public String getEmail() {
    return user.getEmail();
  }
}