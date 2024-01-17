package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class SecurityUser implements UserDetails {
  private Boolean isAdmin;
  private Boolean isVerified;

  private User user;

  public SecurityUser() {
  }

  public SecurityUser(User user) {
    this.user = user;
    isVerified = true;
    isAdmin = false;
  }

  public SecurityUser(User user, boolean isAdmin, boolean isVerified) {
    this.user = user;
    this.isAdmin = isAdmin;
    this.isVerified = isVerified;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(user
            .getRole()
            .split(","))
        .map(role -> new SimpleGrantedAuthority(role.trim()))
        .toList();
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