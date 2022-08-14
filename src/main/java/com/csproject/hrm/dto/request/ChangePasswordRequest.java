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
public class ChangePasswordRequest {
  @NotBlank(message = "Email must not be blank!")
  @Pattern(regexp = EMAIL_VALIDATION, message = "Email is not valid!")
  private String email;

  @NotBlank(message = "Old Password must not be blank!")
  @Pattern(regexp = PASSWORD_VALIDATION, message = "Old Password is not valid!")
  private String old_password;

  @NotBlank(message = "New Password must not be blank!")
  @Pattern(regexp = PASSWORD_VALIDATION, message = "New Password is not valid!")
  private String new_password;

  @NotBlank(message = "Re Password must not be blank!")
  @Pattern(regexp = PASSWORD_VALIDATION, message = "Re Password is not valid!")
  private String re_password;
}
