package com.sbakic.usermanagement.logging;

import com.sbakic.usermanagement.security.AuthorityRole;
import com.sbakic.usermanagement.security.SecurityUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoggerInterceptor implements HandlerInterceptor {

  @Value("${log.file}")
  private String logFile;

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView) {
    if (request.isUserInRole(AuthorityRole.ADMIN.getRole())) {
      String currentUseEmail = SecurityUtils.getCurrentUserLogin()
          .orElseThrow(() -> new RuntimeException("Couldn't get current user."));

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      String message = String.format("%s  [ %s ][ %s ][ %s ]",
          sdf.format(new Date()),
          request.getMethod(),
          request.getRequestURI(),
          currentUseEmail);

      try (FileWriter fileWriter = new FileWriter(logFile, true)) {
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(message);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
