package com.csproject.hrm.dto.chart;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralChart {
  private String label;
  private int value;
}