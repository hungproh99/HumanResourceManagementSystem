package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryMonthlyDto {
  private Long salaryMonthlyId;
  private Double standardPoint;
  private Double actualPoint;
  private Double otPoint;
  private BigDecimal totalDeduction;
  private BigDecimal totalBonus;
  private BigDecimal totalInsurance;
  private BigDecimal totalTax;
  private BigDecimal totalAdvance;
  private BigDecimal finalSalary;
}
