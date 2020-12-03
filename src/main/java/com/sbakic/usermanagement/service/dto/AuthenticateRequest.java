package com.sbakic.usermanagement.service.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthenticateRequest {

  @NotNull
  private String email;

  @NotNull
  private String password;

}
