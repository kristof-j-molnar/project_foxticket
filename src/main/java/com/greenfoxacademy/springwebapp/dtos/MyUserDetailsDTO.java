package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetailsDTO implements UserDetails {
  private Integer userId;
  private String userName;
  private String email;
  private String password;
  private Boolean isAdmin;
  private Boolean isVerified;
  private List<GrantedAuthority> roles;

    public MyUserDetailsDTO() {
    }

    public MyUserDetailsDTO(User user) {
      this.userId = user.getId();
      this.userName = user.getName();
      this.email = user.getEmail();
      this.password = user.getPassword();
      this.isVerified = true;
      this.isAdmin = false;
      this.roles = Arrays.stream(user.getRole().split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
    }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
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
    return userId;
  }

  public Boolean getIsAdmin() {
    return isAdmin;
  }

  public Boolean getIsVerified() {
    return isVerified;
  }
}