package com.csproject.hrm.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static com.csproject.hrm.common.constant.Constants.EMAIL_VALIDATION;
import static com.csproject.hrm.common.constant.Constants.NUMERIC_VALIDATION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAdditionalInfoRequest {
	@NotBlank(message = "Address must not be blank!")
	private String address;
	
	@NotBlank(message = "Place Of Residence must not be blank!")
	private String place_of_residence;
	
	@NotBlank(message = "Place Of Origin must not be blank!")
	private String place_of_origin;
	
	@NotBlank(message = "Nationality must not be blank!")
	private String nationality;
	
	@NotBlank(message = "Card ID must not be blank!")
	@Pattern(regexp = NUMERIC_VALIDATION, message = "card_id accept numeric only!")
	private String card_id;
	
	@Past(message = "Provide Date must less than today!")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate provideDate;
	
	@NotBlank(message = "Provide Place must not be blank!")
	private String providePlace;
	
	@Pattern(regexp = EMAIL_VALIDATION, message = "Personal Email is not valid!!")
	private String personal_email;
	
	@NotBlank(message = "Phone Number must not be blank!")
	@Pattern(regexp = NUMERIC_VALIDATION, message = "Phone Number accept numeric only!")
	private String phone_number;
	
	private String nick_name;
	
	private String facebook;
	
	@NotBlank(message = "Employee ID must not be blank!")
	private String employee_id;
}