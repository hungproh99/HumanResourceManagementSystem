package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AvatarRequest {
	@NotBlank(message = "Avatar must not be blank!")
	private String avatar;
	
	@NotBlank(message = "Employee ID must not be blank!")
	private String employeeId;
}