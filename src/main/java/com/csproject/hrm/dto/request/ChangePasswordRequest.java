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
  @NotBlank(message = "email must not be blank")
  @Pattern(regexp = EMAIL_VALIDATION, message = "email is not valid")
  private String email;

  @NotBlank(message = "old_password must not be blank")
  @Pattern(regexp = PASSWORD_VALIDATION, message = "old_password is not valid")
  private String old_password;

  @NotBlank(message = "new_password must not be blank")
  @Pattern(regexp = PASSWORD_VALIDATION, message = "new_password is not valid")
  private String new_password;

  @NotBlank(message = "re_password must not be blank")
  @Pattern(regexp = PASSWORD_VALIDATION, message = "re_password is not valid")
  private String re_password;
}
