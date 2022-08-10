package com.csproject.hrm.dto.chart;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralChart {
  private String label;
  private int value;
}