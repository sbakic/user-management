package com.sbakic.usermanagement.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.GenericFilterBean;

@AllArgsConstructor
public class JWTFilter extends GenericFilterBean {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_TOKEN_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String jwt = resolveToken(request);
    if (StringUtils.isNotBlank(jwt) && this.tokenProvider.validateToken(jwt)) {
      Authentication authentication = this.tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    chain.doFilter(request, response);
  }

  private String resolveToken(ServletRequest request) {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String bearerToken = httpRequest.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
      return bearerToken.replace(BEARER_TOKEN_PREFIX, StringUtils.EMPTY);
    }
    return null;
  }

}
