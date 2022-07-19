package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.chart.EmployeeChart;
import com.csproject.hrm.dto.dto.WorkingTypeDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartRepositoryCustom {
  String getAreaNameByEmployeeID(String employeeID);

  int countTotalEmployeeByAreaName(String areaName);

  int countTotalEmployeeByGenderAndAreaName(String areaName, String gender);

  List<Integer> getAllEmployeeSeniorityForChartByAreaName(String areaName);

  List<Integer> getAllEmployeeAgeForChartByAreaName(String areaName);

  List<WorkingTypeDto> getAllWorkingType();

  int countTotalEmployeeByContractTypeAndAreaName(String areaName, Long contractTypeID);

  List<EmployeeChart> getManagerByAreaName(String areaName);

  List<EmployeeChart> getEmployeeByManagerID(String managerID);
}