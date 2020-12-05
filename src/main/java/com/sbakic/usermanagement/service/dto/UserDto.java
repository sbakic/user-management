package com.sbakic.usermanagement.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Builder
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
