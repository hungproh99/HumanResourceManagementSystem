package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.csproject.hrm.common.constant.Constants.EMAIL_VALIDATION;
import static com.csproject.hrm.common.constant.Constants.PASSWORD_VALIDATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
  @NotBlank(message = "email must not be blank!")
  @Pattern(regexp = EMAIL_VALIDATION, message = "email is not valid!")
  private String email;

  @NotBlank(message = "password must not be blank!")
  @Pattern(regexp = PASSWORD_VALIDATION, message = "password is not valid!")
  private String password;
}
