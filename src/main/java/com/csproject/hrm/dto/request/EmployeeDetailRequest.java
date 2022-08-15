package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeDetailRequest {
	@NotBlank(message = "Full Name must not be blank!")
	private String full_name;
	
	@NotBlank(message = "Employee ID must not be blank!")
	private String employee_id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate start_date;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end_date;
	
	private Boolean working_status;
	
	@NotBlank(message = "Contract must not be blank!")
	private String contract_url;
	
	@NotBlank(message = "Phone Number must not be blank!")
	private String phone_number;
	
	@Positive(message = "Grade must be a positive number!")
	private Long grade_id;
	
	@Past(message = "Birth Date must less than today!")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birth_date;
	
	@NotBlank(message = "Company Email must not be blank!")
	private String company_email;
	
	@NotBlank(message = "Gender must not be blank!")
	private String gender;
	
	private String marital_status;
	
	@Positive(message = "Office must be a positive number!")
	private Long office_id;
	
	@Positive(message = "Job must be a positive number!")
	private Long job_id;
	
	@Positive(message = "Area must be a positive number!")
	private Long area_id;
	
	@Positive(message = "Working Contract ID must be a positive number!")
	private Long working_contract_id;
	
	@Positive(message = "Working Place ID must be a positive number!")
	private Long working_place_id;
}