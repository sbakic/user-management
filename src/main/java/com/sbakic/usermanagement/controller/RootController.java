package com.sbakic.usermanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

  @RequestMapping("/")
  public String root() {
    return "Welcome to User Management Service";
  }
}
