package com.csproject.hrm.services;

import com.csproject.hrm.dto.request.ChangePasswordRequest;
import com.csproject.hrm.dto.request.LoginRequest;
import org.springframework.security.core.Authentication;

public interface LoginService {
  Authentication getAuthentication(LoginRequest loginRequest);

  int changePasswordByUsername(ChangePasswordRequest changePasswordRequest);

  int forgotPasswordByUsername(String email);
}
