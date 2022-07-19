package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EWorkingType;
import com.csproject.hrm.dto.chart.*;
import com.csproject.hrm.dto.dto.WorkingTypeDto;
import com.csproject.hrm.repositories.ChartRepository;
import com.csproject.hrm.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChartServiceImpl implements ChartService {

  @Autowired ChartRepository chartRepository;

  @Override
  public String getAreaNameByEmployeeID(String employeeID) {
    return chartRepository.getAreaNameByEmployeeID(employeeID);
  }

  @Override
  public GeneralDataCharts getGeneralEmployeeDataForChartByAreaName(String areaName) {
    GeneralDataCharts dataCharts = new GeneralDataCharts();

    dataCharts.setTotalEmployee(chartRepository.countTotalEmployeeByAreaName(areaName));

    dataCharts.setTotalMaleEmployee(
        chartRepository.countTotalEmployeeByGenderAndAreaName(areaName, "Male"));

    dataCharts.setTotalFemaleEmployee(
        chartRepository.countTotalEmployeeByGenderAndAreaName(areaName, "Female"));

    dataCharts.setSeniorityList(getSeniority(areaName));

    dataCharts.setAgeList(getAge(areaName));

    dataCharts.setWorkingTypeList(getWorkingType(areaName));

    return dataCharts;
  }

  private List<SeniorityChart> getSeniority(String areaName) {
    List<Integer> seniorityList =
        chartRepository.getAllEmployeeSeniorityForChartByAreaName(areaName);
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("<1", 0);
    map.put("1-2", 0);
    map.put("2-3", 0);
    map.put("3-4", 0);
    map.put("4-5", 0);
    map.put(">5", 0);

    seniorityList.forEach(
        s -> {
          double val = (double) s / 365;
          if (val < 1) {
            map.put("<1", map.get("<1") + 1);
          } else if (val >= 1 && val <= 2) {
            map.put("1-2", map.get("1-2") + 1);
          } else if (val > 2 && val <= 3) {
            map.put("2-3", map.get("2-3") + 1);
          } else if (val > 3 && val <= 4) {
            map.put("3-4", map.get("3-4") + 1);
          } else if (val > 4 && val <= 5) {
            map.put("4-5", map.get("4-5") + 1);
          } else {
            map.put(">5", map.get(">5") + 1);
          }
        });

    List<SeniorityChart> list = new ArrayList<>();
    map.forEach(
        (s, i) -> {
          SeniorityChart chart = new SeniorityChart();
          chart.setLabel(s);
          chart.setValue(i);
          list.add(chart);
        });
    return list;
  }

  private List<AgeChart> getAge(String areaName) {
    List<Integer> ageList = chartRepository.getAllEmployeeAgeForChartByAreaName(areaName);
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("18-24", 0);
    map.put("25-35", 0);
    map.put("36-45", 0);
    map.put("46-55", 0);
    map.put("56-65", 0);
    map.put(">66", 0);

    ageList.forEach(
        s -> {
          double val = (double) s / 365;
          if (val >= 18 && val <= 24) {
            map.put("18-24", map.get("18-24") + 1);
          } else if (val >= 25 && val <= 35) {
            map.put("25-35", map.get("25-35") + 1);
          } else if (val > 36 && val <= 45) {
            map.put("36-45", map.get("36-45") + 1);
          } else if (val > 46 && val <= 55) {
            map.put("46-55", map.get("46-55") + 1);
          } else if (val > 56 && val <= 65) {
            map.put("56-65", map.get("56-65") + 1);
          } else {
            map.put(">66", map.get(">66") + 1);
          }
        });

    List<AgeChart> list = new ArrayList<>();
    map.forEach(
        (s, i) -> {
          AgeChart chart = new AgeChart();
          chart.setLabel(s);
          chart.setValue(i);
          list.add(chart);
        });
    list.sort((o1, o2) -> o1.getLabel().compareToIgnoreCase(o2.getLabel()));
    return list;
  }

  private List<WorkingTypeChart> getWorkingType(String areaName) {
    List<WorkingTypeDto> workingTypeIDs = chartRepository.getAllWorkingType();
    List<WorkingTypeChart> list = new ArrayList<>();

    workingTypeIDs.forEach(
        type -> {
          WorkingTypeChart chart = new WorkingTypeChart();
          chart.setLabel(EWorkingType.getLabel(type.getName()));
          chart.setValue(
              chartRepository.countTotalEmployeeByContractTypeAndAreaName(
                  areaName, type.getType_id()));
          list.add(chart);
        });
    list.sort((o1, o2) -> o1.getLabel().compareToIgnoreCase(o2.getLabel()));
    return list;
  }

  @Override
  public OrganizationalChart getOrganizationalByAreaName(String areaName) {
    OrganizationalChart chart = new OrganizationalChart();

    chart.setTitle(areaName);
    chart.setChildren(getEmployeeByAreaName(areaName));
    return chart;
  }

  private List<EmployeeChart> getEmployeeByAreaName(String areaName) {
    List<EmployeeChart> list = chartRepository.getManagerByAreaName(areaName);

    list.forEach(
        employee -> {
          employee.setChildren(getEmployeeByManagerID(employee.getManagerID()));
        });
    return list;
  }

  private List<EmployeeChart> getEmployeeByManagerID(String managerID) {
    List<EmployeeChart> list = chartRepository.getEmployeeByManagerID(managerID);

    list.forEach(
        employee -> {
          employee.setChildren(getEmployeeByManagerID(employee.getManagerID()));
        });
    return list;
  }
}