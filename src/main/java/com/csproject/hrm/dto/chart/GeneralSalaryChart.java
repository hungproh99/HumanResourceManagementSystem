package com.csproject.hrm.dto.chart;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralSalaryChart {
  private String label;
  private BigDecimal value;
}