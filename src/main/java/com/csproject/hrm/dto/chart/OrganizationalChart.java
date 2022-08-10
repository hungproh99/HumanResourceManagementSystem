package com.csproject.hrm.dto.chart;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationalChart {
  private String title;
  private List<EmployeeChart> children;
}