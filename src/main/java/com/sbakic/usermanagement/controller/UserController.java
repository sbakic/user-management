package com.sbakic.usermanagement.controller;

import com.sbakic.usermanagement.security.IsAdmin;
import com.sbakic.usermanagement.service.UserService;
import com.sbakic.usermanagement.service.dto.UserDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @IsAdmin
  @GetMapping("/users")
  public ResponseEntity<List<UserDto>> listUsers() {
    return ResponseEntity.ok(userService.listUsers());
  }

  @PreAuthorize("hasRole(T(com.sbakic.usermanagement.security.AuthorityRole).ADMIN.name()) or #userId eq @userServiceImpl.DEFAULT_USER")
  @GetMapping("/users/{userId}")
  public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @PreAuthorize("hasRole(T(com.sbakic.usermanagement.security.AuthorityRole).ADMIN.name()) or #userId eq @userServiceImpl.DEFAULT_USER")
  @PutMapping("/users/{userId}")
  public ResponseEntity<UserDto> updateUser(
      @PathVariable String userId,
      @RequestBody @Valid UserDto user
  ) {
    return ResponseEntity.ok(userService.updateUser(userId, user));
  }

}
