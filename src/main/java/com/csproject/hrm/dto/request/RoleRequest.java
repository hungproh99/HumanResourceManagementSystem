package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.csproject.hrm.common.constant.Constants.NUMERIC_VALIDATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleRequest {
  @NotBlank(message = "roleID must not be blank!")
  @Pattern(regexp = NUMERIC_VALIDATION, message = "roleID accept numeric only!")
  private String roleId;

  @NotBlank(message = "employeeID must not be blank!")
  private String employeeId;
}