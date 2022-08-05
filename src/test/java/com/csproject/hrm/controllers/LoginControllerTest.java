package com.csproject.hrm.controllers;

import com.csproject.hrm.common.uri.Uri;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.jwt.UserDetailsImpl;
import com.csproject.hrm.services.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.sample.DataSample.LOGIN_REQUEST;
import static com.csproject.hrm.common.uri.Uri.REQUEST_DETAIL_MAPPING;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LoginController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class LoginControllerTest {
  private static final String REQUEST_MAPPING = Uri.REQUEST_MAPPING + REQUEST_DETAIL_MAPPING;
  //  private static String jwtToken;
  @Autowired private MockMvc mockMvc;
  @Autowired JwtUtils jwtUtils;
  @Autowired @MockBean private LoginService loginService;

  @Test
  void test_login() {
    LoginRequest loginRequest = LOGIN_REQUEST;
    Authentication authentication = loginService.getAuthentication(loginRequest);
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());


  }
}
