package com.csproject.hrm.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static com.csproject.hrm.common.constant.Constants.NUMERIC_VALIDATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingInfoRequest {
  @NotBlank(message = "Employee ID must not be blank!")
  private String employeeId;

  @NotBlank(message = "Base Salary must not be blank!")
  @Pattern(regexp = NUMERIC_VALIDATION, message = "Base Salary accept numeric only!")
  private String baseSalary;

  @NotBlank(message = "Final Salary must not be blank!")
  @Pattern(regexp = NUMERIC_VALIDATION, message = "Final Salary accept numeric only!")
  private String finalSalary;

  @Positive(message = "Office must be a positive number!")
  private Long office;

  @Positive(message = "Area must be a positive number!")
  private Long area;

  @Positive(message = "Position must be a positive number!")
  private Long position;

  @Positive(message = "Grade must be a positive number!")
  private Long grade;

  @Positive(message = "Working Type ID must be a positive number!")
  private Long workingTypeId;

  //  @Past(message = "Start Date must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @Positive(message = "Employee Type must be a positive number!")
  private Long employeeType;

  @NotBlank(message = "Manager ID must not be blank!")
  private String managerId;

  @Positive(message = "Salary Contract ID must be a positive number!")
  private Long salaryContractId;

  @Positive(message = "Working Contract ID must be a positive number!")
  private Long workingContractId;

  @Positive(message = "Working Place ID must be a positive number!")
  private Long workingPlaceId;
}