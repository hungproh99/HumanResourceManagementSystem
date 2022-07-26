package com.csproject.hrm.dto.chart;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveCompanyChart {
  private String label;
  private List<LeaveCompanyChartList> value;
}