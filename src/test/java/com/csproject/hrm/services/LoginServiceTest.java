package com.csproject.hrm.services;

import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.jwt.UserDetailsImpl;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.impl.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.csproject.hrm.common.enums.ERole.ROLE_MANAGER;
import static com.csproject.hrm.common.sample.DataSample.LOGIN_REQUEST;
import static java.lang.reflect.Modifier.PROTECTED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
  @Autowired @Mock GeneralFunction generalFunction;
  @Autowired @Mock AuthenticationManager authenticationManager;
  @Autowired @Mock EmployeeRepository employeeRepository;
  @Autowired @Mock PasswordEncoder passwordEncoder;
  @InjectMocks LoginServiceImpl loginService;
  @Autowired private MockMvc mockMvc;

  @Test
  void test_getAuthentication() throws Exception {
    LoginRequest loginRequest = LOGIN_REQUEST;
    String username = "Nguyen Quang Huy";
    List<GrantedAuthority> authorities =
        new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority(ROLE_MANAGER.name())));
    UserDetailsImpl userDetails =
        new UserDetailsImpl(
            "huynq100", "huynq100@fpt.edu.vn", "Nguyen Quang Huy", "HUy123456789!", authorities);
    Object credentials = PROTECTED;
    Authentication authenticationExpect =
        new UsernamePasswordAuthenticationToken(userDetails, credentials, authorities);
    Mockito.when(employeeRepository.findIdByCompanyEmail(loginRequest.getEmail()))
        .thenReturn(username);

    Mockito.when(
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword())))
        .thenReturn(authenticationExpect);

    Authentication authenticationActual = loginService.getAuthentication(loginRequest);

    Mockito.verify(employeeRepository, Mockito.times(1))
        .findIdByCompanyEmail(loginRequest.getEmail());

    assertEquals("Equals", authenticationExpect, authenticationActual);
  }

  @Test
  void test_getAuthentication_Exception() throws Exception {
    LoginRequest loginRequest = LOGIN_REQUEST;
    Mockito.when(employeeRepository.findIdByCompanyEmail(loginRequest.getEmail())).thenReturn(null);

    assertThrows(
        CustomDataNotFoundException.class, () -> loginService.getAuthentication(loginRequest));
  }
}
