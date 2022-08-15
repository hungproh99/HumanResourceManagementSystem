package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

import static com.csproject.hrm.common.constant.Constants.NUMERIC_VALIDATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BankRequest {
	@Positive(message = "Bank \"ID\" must be a positive number!")
	private Long id;
	
	@NotBlank(message = "Bank Name must not be blank!")
	private String nameBank;
	
	@NotBlank(message = "Address must not be blank!")
	private String address;
	
	@NotBlank(message = "Account Number must not be blank!")
	@Pattern(regexp = NUMERIC_VALIDATION, message = "Account Number accept numeric only!")
	private String accountNumber;
	
	@NotBlank(message = "Account Name must not be blank!")
	private String accountName;
	
	@NotBlank(message = "Employee ID must not be blank!")
	private String employeeId;
}