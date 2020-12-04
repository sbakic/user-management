package com.sbakic.usermanagement.service.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticateRequest {

  @NotNull
  private String email;

  @NotNull
  private String password;

}
