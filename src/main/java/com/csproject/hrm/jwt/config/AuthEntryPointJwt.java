package com.csproject.hrm.jwt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.csproject.hrm.common.constant.Constants.ERROR_AUTHORIZED;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger Logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    Logger.error("Unauthorized error: {}", authException.getMessage());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ERROR_AUTHORIZED);
  }
}