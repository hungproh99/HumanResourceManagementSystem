package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.request.ChangePasswordRequest;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.csproject.hrm.common.constant.Constants.*;

public class LoginServiceImpl implements LoginService {
  @Autowired GeneralFunction generalFunction;
  @Autowired AuthenticationManager authenticationManager;
  @Autowired EmployeeRepository employeeRepository;
  @Autowired PasswordEncoder passwordEncoder;

  public Authentication getAuthentication(LoginRequest loginRequest) {
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();
    String username = employeeRepository.findIdByCompanyEmail(email);
    if (email == null || email.isEmpty()) {
      throw new CustomParameterConstraintException(NOT_EMPTY_EMAIL);
    } else if (!email.matches(EMAIL_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_EMAIL_FORMAT);
    } else if (password == null || password.isEmpty()) {
      throw new CustomParameterConstraintException(NOT_EMPTY_PASSWORD);
    } else if (username == null) {
      throw new CustomDataNotFoundException(NOT_EXIST_USER_WITH + email);
    }
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authentication;
  }

  public int changePasswordByUsername(ChangePasswordRequest changePasswordRequest) {
    String email = changePasswordRequest.getEmail();
    String old_password = changePasswordRequest.getOld_password();
    String new_password = changePasswordRequest.getNew_password();
    String re_password = changePasswordRequest.getRe_password();
    String username = employeeRepository.findIdByCompanyEmail(email);
    String password = employeeRepository.findPasswordById(username);
    String encode_new_password = passwordEncoder.encode(new_password);
    if (email == null || email.isEmpty()) {
      throw new CustomParameterConstraintException(NOT_EMPTY_EMAIL);
    } else if (!email.matches(EMAIL_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_EMAIL_FORMAT);
    } else if (old_password == null
        || old_password.isEmpty()
        || new_password == null
        || new_password.isEmpty()
        || re_password == null
        || re_password.isEmpty()) {
      throw new CustomParameterConstraintException(NOT_EMPTY_PASSWORD);
    } else if (username == null) {
      throw new CustomDataNotFoundException(NOT_EXIST_USER_WITH + email);
    } else if (!passwordEncoder.matches(old_password, password)) {
      throw new CustomParameterConstraintException(WRONG_OLD_PASSWORD);
    } else if (!new_password.equals(re_password)) {
      throw new CustomParameterConstraintException(NOT_MATCH_NEW_PASSWORD);
    } else if (new_password.equals(old_password)) {
      throw new CustomParameterConstraintException(NOT_SAME_OLD_PASSWORD);
    }
    return employeeRepository.updatePassword(encode_new_password, username);
  }

  public int forgotPasswordByUsername(String email) {
    String id = employeeRepository.findIdByCompanyEmail(email);
    if (email == null || email.isEmpty()) {
      throw new CustomParameterConstraintException(NOT_EMPTY_EMAIL);
    } else if (!email.matches(EMAIL_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_EMAIL_FORMAT);
    }
    if (id == null) {
      throw new CustomDataNotFoundException(NOT_EXIST_USER_WITH + email);
    }
    String generatePassword = generalFunction.generateCommonLangPassword();
    String encodePassword = passwordEncoder.encode(generatePassword);
    generalFunction.sendEmail(id, generatePassword, FROM_EMAIL, TO_EMAIL, SEND_PASSWORD_SUBJECT);
    return employeeRepository.updatePassword(encodePassword, id);
  }
}