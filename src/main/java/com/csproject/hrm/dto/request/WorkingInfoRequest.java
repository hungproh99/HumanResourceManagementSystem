package com.csproject.hrm.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingInfoRequest {
  @NotBlank(message = "employeeId must not be blank!")
  private String employeeId;

  @NotBlank(message = "baseSalary must not be blank!")
  private String baseSalary;

  @Positive(message = "office must be a positive number!")
  private Long office;

  @Positive(message = "area must be a positive number!")
  private Long area;

  @Positive(message = "position must be a positive number!")
  private Long position;

  @Positive(message = "grade must be a positive number!")
  private Long grade;

  @Positive(message = "workingTypeId must be a positive number!")
  private Long workingTypeId;

  @Past(message = "startDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @Positive(message = "employeeType must be a positive number!")
  private Long employeeType;

  @NotBlank(message = "managerId must not be blank!")
  private String managerId;

  @Positive(message = "salaryContractId must be a positive number!")
  private Long salaryContractId;

  @Positive(message = "workingContractId must be a positive number!")
  private Long workingContractId;

  @Positive(message = "workingPlaceId must be a positive number!")
  private Long workingPlaceId;
}