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
	@NotBlank(message = "Role ID must not be blank!")
	@Pattern(regexp = NUMERIC_VALIDATION, message = "Role ID accept numeric only!")
	private String roleId;
	
	@NotBlank(message = "Employee ID must not be blank!")
	private String employeeId;
}