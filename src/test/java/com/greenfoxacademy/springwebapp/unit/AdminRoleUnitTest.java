package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.services.UserAuthenticationService;
import com.greenfoxacademy.springwebapp.services.UserAuthenticationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class AdminRoleUnitTest {

  UserAuthenticationService userAuthenticationService = new UserAuthenticationServiceImpl();

  @Test
  void checkIfTheUserHasAdminRole_withValidAdminRole_ShouldReturnTrue() {
    String role = "ADMIN";
    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
    Collection<? extends GrantedAuthority> authorities = List.of(authority);
    Authentication authentication = new UsernamePasswordAuthenticationToken("testAdmin", "test12345", authorities);

    Boolean expectedBoolean = true;
    Boolean actualBoolean = userAuthenticationService.hasRole("Admin", authentication);
    assertEquals(expectedBoolean, actualBoolean);
  }

  @Test
  void checkIfTheUserHasAdminRole_withOnlyUserRole_ShouldReturnFalse() {
    String role1 = "User";
    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role1);
    Collection<? extends GrantedAuthority> authorities = List.of(authority);
    Authentication authentication1 = new UsernamePasswordAuthenticationToken("reka", "reka12345", authorities);

    Boolean expectedBoolean = false;
    Boolean actualBoolean = userAuthenticationService.hasRole("Admin", authentication1);
    Assertions.assertEquals(expectedBoolean, actualBoolean);
  }

  @Test
  void checkIfUserHasAdminRole_withEmptyGrantedRoles_ShouldReturnFalse() {
    Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
    Authentication authentication1 = new UsernamePasswordAuthenticationToken("reka", "reka12345", authorities);

    Boolean expectedBoolean = false;
    Boolean actualBoolean = userAuthenticationService.hasRole("Admin", authentication1);
    Assertions.assertEquals(expectedBoolean, actualBoolean);
  }
}
