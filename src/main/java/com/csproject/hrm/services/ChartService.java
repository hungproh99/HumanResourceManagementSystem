package com.csproject.hrm.services;

import com.csproject.hrm.dto.chart.*;

import java.time.LocalDate;
import java.util.List;

public interface ChartService {
  String getAreaNameByEmployeeID(String employeeID);

  GeneralDataCharts getGeneralEmployeeDataForChartByAreaName(String areaName);

  OrganizationalChart getOrganizational();

  List<LeaveCompanyChart> getLeaveCompanyReasonByYearAndAreaName(Integer year, String areaName);

  List<PaidLeaveChart> getPaidLeaveReasonByYearAndAreaName(Integer year, String areaName);

  List<GeneralSalaryChart> getSalaryStructureByDateAndEmployeeID(LocalDate date, String employeeID);

  List<GeneralSalaryChart> getSalaryHistoryByDateAndEmployeeIDAndType(
      LocalDate date, String employeeID, String type);

  LocalDate getStartDateOfContract(String employeeID);
}