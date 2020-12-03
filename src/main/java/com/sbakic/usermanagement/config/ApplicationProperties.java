package com.sbakic.usermanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Data
public class ApplicationProperties {

  private Security security = new Security();
  private CorsConfiguration cors = new CorsConfiguration();

  @Data
  public static class Security {

    private String base64Secret;
    private Long tokenValidityInSeconds;

  }

}

