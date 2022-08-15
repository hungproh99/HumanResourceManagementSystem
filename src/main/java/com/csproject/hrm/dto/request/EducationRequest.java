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
public class EducationRequest {
	
	@Positive(message = "Education \"ID\" must be a positive number!")
	private Long id;
	
	@NotBlank(message = "School Name must not be blank!")
	private String nameSchool;
	
	@Past(message = "Start Date must less than today!")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	@NotBlank(message = "Certificate must not be blank!")
	private String certificate;
	
	@NotBlank(message = "Status must not be blank!")
	private String status;
	
	@NotBlank(message = "Employee ID must not be blank!")
	private String employeeId;
}