package com.sbakic.usermanagement.service.dto;

import java.util.Set;
import lombok.Data;

@Data
public class UserDto {

  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private String country;
  private String address;
  private Set<AuthorityDto> authorities;

}
