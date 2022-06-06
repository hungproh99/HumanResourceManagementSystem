package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.request.ChangePasswordRequest;
import com.csproject.hrm.dto.request.LoginRequest;
import org.springframework.security.core.Authentication;

public interface LoginServiceImpl {
  Authentication getAuthentication(LoginRequest loginRequest);

  int changePasswordByUsername(ChangePasswordRequest changePasswordRequest);

  int forgotPasswordByUsername(String email);
}