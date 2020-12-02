package com.sbakic.usermanagement.config;

import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.sbakic.usermanagement.repository")
public class DatabaseConfiguration {

  @Value("${spring.h2.tcp.port}")
  private String h2tcpPort;

  @Bean(initMethod = "start", destroyMethod = "stop")
  public Server server() throws SQLException {
    return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2tcpPort);
  }
}
