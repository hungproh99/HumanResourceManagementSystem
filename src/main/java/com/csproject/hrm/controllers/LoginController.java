package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.ChangePasswordRequest;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.dto.response.JwtResponse;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.jwt.UserDetailsImpl;
import com.csproject.hrm.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.REQUEST_FAIL;
import static com.csproject.hrm.common.constant.Constants.REQUEST_SUCCESS;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class LoginController {

  @Autowired LoginService loginService;

  @Autowired JwtUtils jwtUtils;

  @PostMapping(URI_LOGIN)
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = loginService.getAuthentication(loginRequest);
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
    return ResponseEntity.ok(
        new JwtResponse(userDetails.getId(), userDetails.getEmail(), roles, jwt));
    //        return ResponseEntity.ok(passwordEncoder.encode(loginRequest.getPassword()));
  }

  @PutMapping(URI_CHANGE_PASSWORD)
  public ResponseEntity<?> changePassword(
      @RequestBody ChangePasswordRequest changePasswordRequest) {
    int updatePassword = loginService.changePasswordByUsername(changePasswordRequest);
    if (updatePassword == 0) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_FAIL);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(URI_FORGOT_PASSWORD)
  public ResponseEntity<?> forgotPassword(@RequestParam String email) {
    int updatePassword = loginService.forgotPasswordByUsername(email);
    if (updatePassword == 0) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_FAIL);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }
}
