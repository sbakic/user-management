package com.sbakic.usermanagement.controller;

import com.sbakic.usermanagement.security.IsAdmin;
import com.sbakic.usermanagement.service.UserService;
import com.sbakic.usermanagement.service.dto.UserDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

  private final UserService userService;

  @Autowired
  public AuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @IsAdmin
  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDto userDto) {
    userService.registerUser(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
