package com.csproject.hrm.services;

import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.request.ChangePasswordRequest;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.enums.ERole.ROLE_MANAGER;
import static com.csproject.hrm.common.sample.DataSample.*;
import static java.lang.reflect.Modifier.PROTECTED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
  @Autowired @Mock GeneralFunction generalFunction;
  @Autowired @Mock AuthenticationManager authenticationManager;
  @Autowired @Mock EmployeeRepository employeeRepository;
  @Autowired @Mock PasswordEncoder passwordEncoder;
  @InjectMocks LoginServiceImpl loginService;

  @Test
  void test_getAuthentication() {
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
  void test_getAuthentication_Exception() {
    LoginRequest loginRequest = LOGIN_REQUEST;
    Mockito.when(employeeRepository.findIdByCompanyEmail(loginRequest.getEmail())).thenReturn(null);

    assertThrows(
        CustomDataNotFoundException.class, () -> loginService.getAuthentication(loginRequest));
  }

  @Test
  void test_changePasswordByUsername() {
    ChangePasswordRequest changePasswordRequest = CHANGE_PASSWORD_REQUEST;
    String username = "Nguyen Quang Huy";
    String old_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";
    String encode_new_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";

    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn(username);
    Mockito.when(employeeRepository.findPasswordById(anyString())).thenReturn(old_password);
    Mockito.when(passwordEncoder.encode(changePasswordRequest.getNew_password()))
        .thenReturn(encode_new_password);
    Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    Mockito.when(employeeRepository.updatePassword(anyString(), anyString())).thenReturn(1);

    int changePasswordByUsername = loginService.changePasswordByUsername(changePasswordRequest);

    Mockito.verify(employeeRepository, Mockito.times(1))
        .findIdByCompanyEmail(changePasswordRequest.getEmail());
    Mockito.verify(employeeRepository, Mockito.times(1)).findPasswordById(username);
    Mockito.verify(passwordEncoder, Mockito.times(1))
        .encode(changePasswordRequest.getNew_password());

    assertEquals("Equals", 1, changePasswordByUsername);
  }

  @Test
  void test_changePasswordByUsername_NullUserName() {
    ChangePasswordRequest changePasswordRequest = CHANGE_PASSWORD_REQUEST;
    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn(null);
    assertThrows(
        CustomDataNotFoundException.class,
        () -> loginService.changePasswordByUsername(changePasswordRequest));
  }

  @Test
  void test_changePasswordByUsername_WrongOldPass() {
    ChangePasswordRequest changePasswordRequest = CHANGE_PASSWORD_REQUEST;
    String username = "Nguyen Quang Huy";
    String old_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";
    String encode_new_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";
    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn(username);
    Mockito.when(employeeRepository.findPasswordById(anyString())).thenReturn(old_password);
    Mockito.when(passwordEncoder.encode(changePasswordRequest.getNew_password()))
        .thenReturn(encode_new_password);
    Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    assertThrows(
        CustomParameterConstraintException.class,
        () -> loginService.changePasswordByUsername(changePasswordRequest));
  }

  @Test
  void test_changePasswordByUsername_NewPassAndRePassNotMatch() {
    ChangePasswordRequest changePasswordRequest =
        CHANGE_PASSWORD_REQUEST_NEW_PASS_AND_RE_PASS_NOT_MATCH;
    String username = "Nguyen Quang Huy";
    String old_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";
    String encode_new_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";

    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn(username);
    Mockito.when(employeeRepository.findPasswordById(anyString())).thenReturn(old_password);
    Mockito.when(passwordEncoder.encode(changePasswordRequest.getNew_password()))
        .thenReturn(encode_new_password);
    Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    assertThrows(
        CustomParameterConstraintException.class,
        () -> loginService.changePasswordByUsername(changePasswordRequest));
  }

  @Test
  void test_changePasswordByUsername_NewPassAndOldPassSame() {
    ChangePasswordRequest changePasswordRequest =
        CHANGE_PASSWORD_REQUEST_NEW_PASS_AND_OLD_PASS_SAME;
    String username = "Nguyen Quang Huy";
    String old_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";
    String encode_new_password = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";

    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn(username);
    Mockito.when(employeeRepository.findPasswordById(anyString())).thenReturn(old_password);
    Mockito.when(passwordEncoder.encode(changePasswordRequest.getNew_password()))
        .thenReturn(encode_new_password);
    Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    assertThrows(
        CustomParameterConstraintException.class,
        () -> loginService.changePasswordByUsername(changePasswordRequest));
  }

  @Test
  void test_forgotPasswordByUsername() {
    String email = "huynq100@fpt.edu.vn";
    String employeeId = "huynq100";
    String fullName = "Nguyen Quang Huy";
    String generatePassword = "H21@cas1";
    String encodePassword = "$2a$10$759FbdwRznogY3WaPmo2w.SRiCrDYf/PQoHaI7msMqI3ZddfQm4KW";

    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn(employeeId);
    Mockito.when(employeeRepository.getEmployeeNameByEmployeeId(anyString())).thenReturn(fullName);
    Mockito.when(employeeRepository.existsById(anyString())).thenReturn(true);
    Mockito.when(generalFunction.generateCommonLangPassword()).thenReturn(generatePassword);
    Mockito.when(passwordEncoder.encode(anyString())).thenReturn(encodePassword);
    Mockito.doNothing()
        .when(generalFunction)
        .sendEmailForgotPassword(
            fullName, generatePassword, FROM_EMAIL, TO_EMAIL, SEND_PASSWORD_SUBJECT);

    Mockito.when(employeeRepository.updatePassword(anyString(), anyString())).thenReturn(1);

    int changePasswordByUsername = loginService.forgotPasswordByUsername(email);

    Mockito.verify(employeeRepository, Mockito.times(1)).findIdByCompanyEmail(anyString());
    Mockito.verify(generalFunction, Mockito.times(1)).generateCommonLangPassword();
    Mockito.verify(passwordEncoder, Mockito.times(1)).encode(anyString());
    Mockito.verify(generalFunction, Mockito.times(1))
        .sendEmailForgotPassword(
                fullName, generatePassword, FROM_EMAIL, TO_EMAIL, SEND_PASSWORD_SUBJECT);

    assertEquals("Equals", 1, changePasswordByUsername);
  }

  @Test
  void test_forgotPasswordByUsername_NullID() {
    String email = "huynq100@fpt.edu.vn";
    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn(null);
    assertThrows(
        CustomDataNotFoundException.class, () -> loginService.forgotPasswordByUsername(email));
  }

  @Test
  void test_forgotPasswordByUsername_NotExistId() {
    String email = "huynq100@fpt.edu.vn";
    String fullName = "Nguyen Quang Huy";
    Mockito.when(employeeRepository.findIdByCompanyEmail(anyString())).thenReturn("huynq100");
    Mockito.when(employeeRepository.getEmployeeNameByEmployeeId(anyString())).thenReturn(fullName);
    Mockito.when(employeeRepository.existsById(anyString())).thenReturn(false);
    assertThrows(
        CustomDataNotFoundException.class, () -> loginService.forgotPasswordByUsername(email));
  }
}
