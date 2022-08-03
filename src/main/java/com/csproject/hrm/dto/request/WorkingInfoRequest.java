package com.csproject.hrm.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingInfoRequest {
  private String employeeId;
  private String finalSalary;
  private String baseSalary;
  private Long office;
  private Long area;
  private Long position;
  private Long workingTypeId;
  private LocalDate startDate;
}