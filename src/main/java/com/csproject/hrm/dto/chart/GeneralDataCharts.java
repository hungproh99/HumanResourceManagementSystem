package com.csproject.hrm.dto.chart;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralDataCharts {
  private int totalEmployee;
  private int totalMaleEmployee;
  private int totalFemaleEmployee;
  private List<SeniorityChart> seniorityList;
  private List<AgeChart> ageList;
  private List<WorkingTypeChart> workingTypeList;
}