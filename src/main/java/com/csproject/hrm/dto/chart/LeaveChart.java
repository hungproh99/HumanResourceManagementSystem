package com.csproject.hrm.dto.chart;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveChart {
  private String label;
  private List<LeaveCompanyChart> value;
}