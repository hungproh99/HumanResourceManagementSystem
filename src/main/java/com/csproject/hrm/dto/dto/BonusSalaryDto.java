package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonusSalaryDto {
  private Long bonusSalaryId;
  private BigDecimal value;
  private String description;
  private LocalDate date;
  private Long bonusTypeId;
}
