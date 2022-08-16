package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryMonthlyResponse {
  private Long salaryMonthlyId;
  private String employeeId;
  private String fullName;
  private String position;
  private String approverId;
  private Double standardPoint;
  private Double actualPoint;
  private Double otPoint;
  private BigDecimal totalDeduction;
  private BigDecimal totalBonus;
  private BigDecimal totalInsurance;
  private BigDecimal totalTax;
  private BigDecimal totalAdvance;
  private BigDecimal totalAllowance;
  private BigDecimal finalSalary;
  private LocalDate startDate;
  private LocalDate endDate;
  private String salaryStatus;
  private String comment;
  private List<String> checked_by;
}
