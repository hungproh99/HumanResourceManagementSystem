package com.csproject.hrm.dto.chart;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkingTypeChart {
  private String label;
  private int value;
}