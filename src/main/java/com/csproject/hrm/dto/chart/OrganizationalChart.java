package com.csproject.hrm.dto.chart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrganizationalChart {
  private String title;
  private List<EmployeeChart> children;
}