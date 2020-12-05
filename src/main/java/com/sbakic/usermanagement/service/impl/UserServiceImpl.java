package com.sbakic.usermanagement.service.impl;

import com.google.common.collect.Sets;
import com.sbakic.usermanagement.domain.ApplicationUser;
import com.sbakic.usermanagement.domain.Authority;
import com.sbakic.usermanagement.exception.EmailAlreadyTakenException;
import com.sbakic.usermanagement.exception.NotFoundException;
import com.sbakic.usermanagement.repository.AuthorityRepository;
import com.sbakic.usermanagement.repository.UserRepository;
import com.sbakic.usermanagement.security.AuthorityRole;
import com.sbakic.usermanagement.security.SecurityUtils;
import com.sbakic.usermanagement.service.UserService;
import com.sbakic.usermanagement.service.dto.UserDto;
import com.sbakic.usermanagement.service.mapper.UserMapper;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

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

    Set<Authority> authorities = CollectionUtils.isEmpty(userDto.getAuthorities()) ?
        setDefaultAuthorities() : setProvidedAuthorities(userDto);

    ApplicationUser applicationUser = userMapper.toUser(userDto);
    applicationUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    applicationUser.setAuthorities(authorities);

    userRepository.save(applicationUser);
    log.debug("Registered user {}", applicationUser);
  }

  @Override
  public List<UserDto> listUsers() {
    List<ApplicationUser> users = userRepository.findAll();
    log.debug("Listing users {}", users);

    return users.stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public UserDto getUser(String userId) {
    String email = validateUser(userId);

    ApplicationUser user = userRepository.findUserByEmail(email)
        .orElseThrow(() -> new NotFoundException(
            String.format("Couldn't find user with email '%s'.", email)));
    log.debug("Get user {}", user);

    return userMapper.toDto(user);
  }

  @Override
  public UserDto updateUser(String userId, UserDto updateUser) {
    return null;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String login) {
    log.debug("Authenticating {}", login);

    String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
    return userRepository.findOneWithAuthoritiesByEmail(lowercaseLogin)
        .map(this::createSpringSecurityUser)
        .orElseThrow(() -> new UsernameNotFoundException(
            String.format("User '%s' was not found in the database", lowercaseLogin)));
  }

  private Set<Authority> setDefaultAuthorities() {
    return Sets.newHashSet(findAuthority(AuthorityRole.USER.getRole()));
  }

  private Set<Authority> setProvidedAuthorities(UserDto userDto) {
    return userDto.getAuthorities()
        .stream()
        .map(authorityDto -> findAuthority(authorityDto.getName()))
        .collect(Collectors.toSet());
  }

  private Authority findAuthority(String authorityName) {
    return authorityRepository.findById(authorityName)
        .orElseThrow(() -> new RuntimeException("User authority not found."));
  }

  private User createSpringSecurityUser(ApplicationUser applicationUser) {
    List<GrantedAuthority> grantedAuthorities = applicationUser.getAuthorities()
        .stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
        .collect(Collectors.toList());
    return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
  }

  private String validateUser(String userId) {
    if (userId.equals(DEFAULT_USER)) {
      return SecurityUtils.getCurrentUserLogin()
          .orElseThrow(() -> new RuntimeException("Couldn't get current user."));
    }

    return userId;
  }

}
