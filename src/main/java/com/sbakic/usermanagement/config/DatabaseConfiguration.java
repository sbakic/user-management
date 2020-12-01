package com.sbakic.usermanagement.config;

import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

  @Value("${spring.h2.tcp.port}")
  private String h2tcpPort;

  @Bean(initMethod = "start", destroyMethod = "stop")
  public Server server() throws SQLException {
    return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2tcpPort);
  }
}
