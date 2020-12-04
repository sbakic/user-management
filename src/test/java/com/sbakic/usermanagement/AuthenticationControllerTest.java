package com.sbakic.usermanagement;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbakic.usermanagement.domain.ApplicationUser;
import com.sbakic.usermanagement.domain.Authority;
import com.sbakic.usermanagement.repository.AuthorityRepository;
import com.sbakic.usermanagement.repository.UserRepository;
import com.sbakic.usermanagement.security.AuthorityRole;
import com.sbakic.usermanagement.service.dto.AuthenticateRequest;
import com.sbakic.usermanagement.service.dto.AuthenticateResponse;
import com.sbakic.usermanagement.service.dto.UserDto;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest {

  @Autowired
  private MockMvc restAccountMockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private final UserDto registerUser = UserDto.builder()
      .firstName("Stefan")
      .lastName("Bakic")
      .email("sbakic@example.com")
      .password("password")
      .country("Serbia")
      .address("New Belgrade")
      .build();

  private final ApplicationUser existingAdminUser = ApplicationUser.builder()
      .firstName("Existing Admin")
      .email("existingadmin@example.com")
      .password("$2y$10$XcfMSC4rYvOd4edLoFCsDOaToDiSltknNcwXOyOAXNl0L2OSIAVUe")
      .build();

  private final ApplicationUser existingRegularUser = ApplicationUser.builder()
      .firstName("Existing Regular")
      .email("existingregular@example.com")
      .password("$2y$10$XcfMSC4rYvOd4edLoFCsDOaToDiSltknNcwXOyOAXNl0L2OSIAVUe")
      .build();

  private final Authority adminRole = Authority.builder()
      .name(AuthorityRole.ADMIN.getRole())
      .build();

  private final Authority userRole = Authority.builder()
      .name(AuthorityRole.USER.getRole())
      .build();

  @BeforeEach
  @Transactional
  void setUp() {
    userRepository.deleteAll();

    authorityRepository.saveAll(Arrays.asList(adminRole, userRole));

    existingAdminUser.setAuthorities(Sets.newHashSet(Collections.singletonList(adminRole)));
    existingRegularUser.setAuthorities(Sets.newHashSet(Collections.singletonList(userRole)));

    userRepository.saveAll(Arrays.asList(existingAdminUser, existingRegularUser));
  }

  @Test
  @Transactional
  void authorize_givenExistingUser_shouldReturnJwt() throws Exception {
    MvcResult result = callAuthenticate("existingadmin@example.com", "password", status().isOk());

    String idToken = getIdTokenFromResponse(result);

    assertTrue(idToken.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"));
  }

  @Test
  @Transactional
  void authorize_givenNonexistentUser_shouldReturnStatus401() throws Exception {
    callAuthenticate("invalid@example.com", "invalid", status().isUnauthorized());
  }

  @Test
  @Transactional
  void registerUser_givenExistingAdminUserAndNonexistentUser_shouldReturnStatus201()
      throws Exception {
    MvcResult result = callAuthenticate("existingadmin@example.com", "password", status().isOk());

    String idToken = getIdTokenFromResponse(result);

    callRegister(registerUser, idToken, status().isCreated());
  }

  @Test
  @Transactional
  void registerUser_givenExistingRegularUserAndNonexistentUser_shouldReturnStatus403()
      throws Exception {
    MvcResult result = callAuthenticate("existingregular@example.com", "password", status().isOk());

    String idToken = getIdTokenFromResponse(result);

    callRegister(registerUser, idToken, status().isForbidden());
  }

  @Test
  @Transactional
  void registerUser_givenExistingAdminUserAndInvalidRequest_shouldReturnStatus400()
      throws Exception {
    MvcResult result = callAuthenticate("existingadmin@example.com", "password", status().isOk());

    String idToken = getIdTokenFromResponse(result);

    callRegister("", "", idToken, status().isBadRequest());
  }

  @Test
  @Transactional
  void registerUser_givenExistingAdminUserAndExistingUser_shouldReturnStatus409()
      throws Exception {
    MvcResult result = callAuthenticate("existingadmin@example.com", "password", status().isOk());

    String idToken = getIdTokenFromResponse(result);

    callRegister("existingregular@example.com", "password", idToken, status().isConflict());
  }

  private MvcResult callAuthenticate(String email, String password, ResultMatcher matcher)
      throws Exception {
    return restAccountMockMvc.perform(
        post("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                AuthenticateRequest.builder()
                    .email(email)
                    .password(password)
                    .build())))
        .andDo(print())
        .andExpect(matcher)
        .andReturn();
  }

  private String getIdTokenFromResponse(MvcResult result) throws Exception {
    AuthenticateResponse response = objectMapper.readValue(
        result.getResponse().getContentAsString(), AuthenticateResponse.class);
    return response.getIdToken();
  }

  private void callRegister(UserDto registerUser, String idToken, ResultMatcher matcher)
      throws Exception {
    restAccountMockMvc.perform(
        post("/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", String.format("Bearer %s", idToken))
            .content(objectMapper.writeValueAsString(registerUser)))
        .andDo(print())
        .andExpect(matcher)
        .andReturn();
  }

  private void callRegister(String email, String password, String idToken, ResultMatcher matcher)
      throws Exception {
    restAccountMockMvc.perform(
        post("/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", String.format("Bearer %s", idToken))
            .content(objectMapper.writeValueAsString(
                AuthenticateRequest.builder()
                    .email(email)
                    .password(password)
                    .build())))
        .andDo(print())
        .andExpect(matcher)
        .andReturn();
  }

}
