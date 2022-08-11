package com.csproject.hrm.dto.chart;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralSalaryChart {
  private String label;
  private BigDecimal value;
}