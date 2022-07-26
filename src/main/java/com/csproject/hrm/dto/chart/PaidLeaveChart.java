package com.csproject.hrm.dto.chart;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaidLeaveChart {
  private String label;
  private List<PaidLeaveChartList> value;
}