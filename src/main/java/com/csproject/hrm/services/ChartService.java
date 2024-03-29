package com.csproject.hrm.services;

import com.csproject.hrm.dto.chart.*;
import com.csproject.hrm.dto.dto.PaidLeaveReasonDto;

import java.time.LocalDate;
import java.util.List;

public interface ChartService {
  String getAreaNameByEmployeeID(String employeeID);

  GeneralDataCharts getGeneralEmployeeDataForChartByAreaName(String employeeId, String areaName);

  OrganizationalChart getOrganizational();

  List<LeaveCompanyChart> getLeaveCompanyReasonByYearAndAreaName(Integer year, String areaName);

  List<PaidLeaveChart> getPaidLeaveReasonByYearAndManagerID(
      String headerAuth, Integer year, String employeeID);

  List<GeneralSalaryChart> getSalaryStructureByDateAndEmployeeID(LocalDate date, String employeeID);

  List<GeneralSalaryChart> getSalaryHistoryByDateAndEmployeeIDAndType(
      LocalDate date, String employeeID, String type);

  LocalDate getStartDateOfContract(String employeeID);

  List<PaidLeaveReasonDto> getAllPaidLeaveReason();
}