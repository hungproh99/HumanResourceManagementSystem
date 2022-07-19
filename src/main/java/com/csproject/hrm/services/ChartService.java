package com.csproject.hrm.services;

import com.csproject.hrm.dto.chart.GeneralDataCharts;
import com.csproject.hrm.dto.chart.OrganizationalChart;

public interface ChartService {
  String getAreaNameByEmployeeID(String employeeID);

  GeneralDataCharts getGeneralEmployeeDataForChartByAreaName(String areaName);

  OrganizationalChart getOrganizationalByAreaName(String areaName);
}