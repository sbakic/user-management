package com.sbakic.usermanagement.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticateResponse {

  private String idToken;

}
