package com.sbakic.usermanagement.service.impl;

import com.google.common.collect.Sets;
import com.sbakic.usermanagement.domain.ApplicationUser;
import com.sbakic.usermanagement.domain.Authority;
import com.sbakic.usermanagement.exception.EmailAlreadyTakenException;
import com.sbakic.usermanagement.repository.AuthorityRepository;
import com.sbakic.usermanagement.repository.UserRepository;
import com.sbakic.usermanagement.security.AuthorityRole;
import com.sbakic.usermanagement.service.UserService;
import com.sbakic.usermanagement.service.dto.UserDto;
import com.sbakic.usermanagement.service.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private final AuthorityRepository authorityRepository;
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
      AuthorityRepository authorityRepository,
      UserMapper userMapper,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder
  ) {
    this.authorityRepository = authorityRepository;
    this.userMapper = userMapper;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void registerUser(UserDto userDto) {
    userRepository.findUserByEmail(userDto.getEmail())
        .ifPresent(user -> {
          throw new EmailAlreadyTakenException(
              String.format("Email '%s' is already taken.", userDto.getEmail()));
        });

    Authority userAuthority = authorityRepository.findById(AuthorityRole.USER.getRole())
        .orElseThrow(() -> new RuntimeException("User authority not found."));

    ApplicationUser applicationUser = userMapper.toUser(userDto);
    applicationUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    applicationUser.setAuthorities(Sets.newHashSet(userAuthority));

    userRepository.save(applicationUser);
    log.debug("Registered user {}", applicationUser);
  }

}
