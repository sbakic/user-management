package com.sbakic.usermanagement.service;

import com.sbakic.usermanagement.service.dto.UserDto;
import java.util.List;

public interface UserService {

  String DEFAULT_USER = "-me-";

  void registerUser(UserDto userDto);

  List<UserDto> listUsers(UserDto filterUser);

  UserDto getUser(String userId);

  UserDto updateUser(String userId, UserDto updateUser);

}
