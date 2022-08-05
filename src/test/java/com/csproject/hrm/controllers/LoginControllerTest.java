package com.csproject.hrm.controllers;

import com.csproject.hrm.common.configs.JacksonConfig;
import com.csproject.hrm.dto.request.ChangePasswordRequest;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.dto.response.JwtResponse;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.jwt.UserDetailsImpl;
import com.csproject.hrm.services.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.csproject.hrm.common.enums.ERole.ROLE_MANAGER;
import static com.csproject.hrm.common.sample.DataSample.*;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;
import static java.lang.reflect.Modifier.PROTECTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LoginController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class LoginControllerTest {
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private JwtUtils jwtUtils;
  @Autowired @MockBean private LoginService loginService;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void test_login() throws Exception {
    LoginRequest loginRequest = LOGIN_REQUEST;
    List<GrantedAuthority> authorities =
        new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority(ROLE_MANAGER.name())));
    UserDetailsImpl userDetails =
        new UserDetailsImpl(
            "huynq100", "huynq100@fpt.edu.vn", "Nguyen Quang Huy", "HUy123456789!", authorities);
    Object credentials = PROTECTED;
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(userDetails, credentials, authorities);
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NTk3MTkyMjMsImV4cCI6MTY1OTgwNTYyM30.vPn7J72tTFwpJ0cKtSbLYpuwM41ZdIo5z6pjMpkktU7BN7EMHpCarEhRs-rjC4XJUzq9sRW6lTH_OxQQF5M0Vg";
    Mockito.when(loginService.getAuthentication(loginRequest)).thenReturn(authentication);
    Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/login")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(loginRequest))
            .characterEncoding(StandardCharsets.UTF_8);

    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    JwtResponse expected = JWT_RESPONSE;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    JwtResponse jwtResponse = mapper.readValue(actual, JwtResponse.class);
    assertEquals(expected, jwtResponse);
  }

  @Test
  void test_changePassword() throws Exception {
    ChangePasswordRequest changePasswordRequest = CHANGE_PASSWORD_REQUEST;
    Mockito.when(loginService.changePasswordByUsername(changePasswordRequest)).thenReturn(1);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/change_password")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(changePasswordRequest))
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_forgotPassword() throws Exception {
    Mockito.when(loginService.forgotPasswordByUsername("huynq100@fpt.edu.vn")).thenReturn(1);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/forgot_password")
            .accept("*/*")
            .param("email", "huynq100@fpt.edu.vn")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }
}
