package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryMonthlyInfoDto {
  private String employeeId;
  private LocalDate startDate;
  private LocalDate endDate;
}
