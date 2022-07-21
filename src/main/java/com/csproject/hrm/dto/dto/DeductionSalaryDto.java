package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionSalaryDto {
  private Long deductionSalaryId;
  private BigDecimal value;
  private String description;
  private LocalDate date;
  private Long deductionTypeId;
}
