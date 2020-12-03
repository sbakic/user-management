package com.sbakic.usermanagement.controller;

import com.sbakic.usermanagement.security.IsAdmin;
import com.sbakic.usermanagement.security.jwt.TokenProvider;
import com.sbakic.usermanagement.service.UserService;
import com.sbakic.usermanagement.service.dto.AuthenticateRequest;
import com.sbakic.usermanagement.service.dto.AuthenticateResponse;
import com.sbakic.usermanagement.service.dto.UserDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

  private final UserService userService;
  private final TokenProvider tokenProvider;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @Autowired
  public AuthenticationController(
      UserService userService,
      TokenProvider tokenProvider,
      AuthenticationManagerBuilder authenticationManagerBuilder
  ) {
    this.userService = userService;
    this.tokenProvider = tokenProvider;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
  }

  @IsAdmin
  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDto userDto) {
    userService.registerUser(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticateResponse> authorize(
      @Valid @RequestBody AuthenticateRequest authenticateRequest
  ) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        authenticateRequest.getEmail(),
        authenticateRequest.getPassword());

    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    SecurityContextHolder.getContext()
        .setAuthentication(authentication);

    String jwt = tokenProvider.createToken(authentication);
    return new ResponseEntity<>(new AuthenticateResponse(jwt), HttpStatus.OK);
  }

}
