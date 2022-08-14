package com.csproject.hrm.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingInfoRequest {
  @NotBlank(message = "employeeId must not be blank!")
  private String employeeId;

  @NotBlank(message = "baseSalary must not be blank!")
  private String baseSalary;

  @NotBlank(message = "finalSalary must not be blank!")
  private String finalSalary;

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