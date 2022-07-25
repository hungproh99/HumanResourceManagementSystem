package com.csproject.hrm.dto.chart;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaidLeaveChartList {
  private String reason;
  private Integer value;
}