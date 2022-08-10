package com.csproject.hrm.dto.chart;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeChart {
  private String employeeID;
  private String title;
  private String name;
  private String avatar;
  private List<EmployeeChart> children;
}