package com.csproject.hrm.dto.chart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EmployeeChart {
  private String employeeID;
  private String title;
  private String name;
  private String avatar;
  private List<EmployeeChart> children;
}