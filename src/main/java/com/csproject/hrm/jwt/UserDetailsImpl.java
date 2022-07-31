package com.csproject.hrm.jwt;

import com.csproject.hrm.entities.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1L;

  private String id;

  private String email;
  private String name;

  @JsonIgnore private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(
      String id,
      String email,
      String name,
      String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(Employee employee) {
    List<GrantedAuthority> authorities =
        new ArrayList<>(
            Arrays.asList(new SimpleGrantedAuthority(employee.getRoleType().getERole().name())));
    return new UserDetailsImpl(
        employee.getId(),
        employee.getCompanyEmail(),
        employee.getFullName(),
        employee.getPassword(),
        authorities);
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName() {
    return name;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return null;
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    UserDetailsImpl userDetails = (UserDetailsImpl) obj;
    return Objects.equals(id, userDetails.id);
  }
}
