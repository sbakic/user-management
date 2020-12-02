package com.sbakic.usermanagement.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AuthorityRole {
  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");

  @Getter
  private final String role;

}
