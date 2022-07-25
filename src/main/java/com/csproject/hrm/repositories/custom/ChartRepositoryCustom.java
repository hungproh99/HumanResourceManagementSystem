package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.chart.EmployeeChart;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

  EmployeeChart getManagerByManagerID(String managerID);

  List<EmployeeChart> getEmployeeByManagerID(String managerID);

  List<LeaveCompanyReasonDto> getAllLeaveCompanyReason();

  int countLeaveCompanyReasonByDateAndReasonID(
      LocalDate startDate, LocalDate endDate, Long reasonID, String areaName);

  List<PaidLeaveReasonDto> getAllPaidLeaveReason();

  int countPaidLeaveReasonByDateAndReasonID(
      LocalDate startDate, LocalDate endDate, Long reasonID, String areaName);

  List<SalaryMonthlyResponse> getSalaryHistoryByDateAndEmployeeIDAndType(
      LocalDate startDate, LocalDate endDate, String employeeID, String type);

  LocalDate getStartDateOfContract(String employeeID);
}