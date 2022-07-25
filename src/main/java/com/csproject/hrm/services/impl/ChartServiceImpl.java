package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.chart.*;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.ChartService;
import com.csproject.hrm.services.SalaryMonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class ChartServiceImpl implements ChartService {

  @Autowired ChartRepository chartRepository;
  @Autowired WorkingPlaceRepository contractRepository;
  @Autowired SalaryMonthlyRepository salaryMonthlyRepository;
  @Autowired SalaryMonthlyService salaryMonthlyService;

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

  private List<GeneralChart> getSeniority(String areaName) {
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

    List<GeneralChart> list = new ArrayList<>();
    map.forEach(
        (s, i) -> {
          GeneralChart chart = new GeneralChart();
          chart.setLabel(s);
          chart.setValue(i);
          list.add(chart);
        });
    return list;
  }

  private List<GeneralChart> getAge(String areaName) {
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

    List<GeneralChart> list = new ArrayList<>();
    map.forEach(
        (s, i) -> {
          GeneralChart chart = new GeneralChart();
          chart.setLabel(s);
          chart.setValue(i);
          list.add(chart);
        });
    list.sort((o1, o2) -> o1.getLabel().compareToIgnoreCase(o2.getLabel()));
    return list;
  }

  private List<GeneralChart> getWorkingType(String areaName) {
    List<WorkingTypeDto> workingTypeIDs = chartRepository.getAllWorkingType();
    List<GeneralChart> list = new ArrayList<>();

    workingTypeIDs.forEach(
        type -> {
          GeneralChart chart = new GeneralChart();
          chart.setLabel(EWorkingType.getLabel(type.getName()));
          chart.setValue(
              chartRepository.countTotalEmployeeByContractTypeAndAreaName(
                  areaName, type.getType_id()));
          list.add(chart);
        });
    return list;
  }

  @Override
  public OrganizationalChart getOrganizational() {
    OrganizationalChart chart = new OrganizationalChart();
    chart.setTitle("HRM");
    chart.setChildren(getAreaManager());

    return chart;
  }

  private List<EmployeeChart> getAreaManager() {
    List<AreaDto> areaList = contractRepository.getListArea();
    List<EmployeeChart> list = new ArrayList<>();
    areaList.forEach(
        area -> {
          EmployeeChart employee = chartRepository.getManagerByManagerID(area.getManager_id());
          employee.setTitle(EArea.getLabel(area.getName()));
          employee.setChildren(
              getEmployeeByManagerID(employee.getEmployeeID(), EArea.getLabel(area.getName())));
          list.add(employee);
        });
    return list;
  }

  private List<EmployeeChart> getEmployeeByManagerID(String managerID, String areaName) {
    List<EmployeeChart> list = chartRepository.getEmployeeByManagerID(managerID);
    AtomicInteger i = new AtomicInteger(1);

    list.forEach(
        employee -> {
          employee.setTitle(areaName + "-" + i.getAndIncrement());
          employee.setChildren(
              getEmployeeByManagerID(employee.getEmployeeID(), employee.getTitle()));
        });
    return list;
  }

  @Override
  public List<LeaveCompanyChart> getLeaveCompanyReasonByYearAndAreaName(
      Integer year, String areaName) {
    List<LeaveCompanyChart> list = new ArrayList<>();
    List<LeaveCompanyReasonDto> LeaveCompanyReasonList = chartRepository.getAllLeaveCompanyReason();

    for (int i = 0; i <= 2; i++) {
      LeaveCompanyChart chart = new LeaveCompanyChart();
      chart.setLabel("Q" + (i + 1) + " " + year);

      List<LeaveCompanyChartList> leaveCompanyChartList = new ArrayList<>();
      int finalI = i * 4;
      LeaveCompanyReasonList.forEach(
          leaveCompanyReason -> {
            Month month = Month.of(finalI + 4);
            LocalDate startDate = LocalDate.of(year, finalI + 1, 1);
            LocalDate endDate = LocalDate.of(year, month, month.length(startDate.isLeapYear()));

            LeaveCompanyChartList leaveCompanyChart = new LeaveCompanyChartList();

            leaveCompanyChart.setReason(leaveCompanyReason.getReason_name());
            leaveCompanyChart.setValue(
                chartRepository.countLeaveCompanyReasonByDateAndReasonID(
                    startDate, endDate, leaveCompanyReason.getReason_id(), areaName));

            leaveCompanyChartList.add(leaveCompanyChart);
          });

      chart.setValue(leaveCompanyChartList);
      list.add(chart);
    }
    return list;
  }

  @Override
  public List<PaidLeaveChart> getPaidLeaveReasonByYearAndAreaName(Integer year, String areaName) {
    List<PaidLeaveChart> list = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      PaidLeaveChart chart = new PaidLeaveChart();
      chart.setLabel(
          Month.of(i).getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US) + " " + year);

      List<PaidLeaveReasonDto> paidLeaveReasonList = chartRepository.getAllPaidLeaveReason();

      List<PaidLeaveChartList> paidLeaveChartList = new ArrayList<>();
      int finalI = i;
      paidLeaveReasonList.forEach(
          paidLeaveReason -> {
            LocalDate startDate = LocalDate.of(year, finalI, 1);
            LocalDate endDate =
                LocalDate.of(year, finalI, startDate.getMonth().length(startDate.isLeapYear()));

            PaidLeaveChartList paidLeaveChart = new PaidLeaveChartList();

            paidLeaveChart.setReason(paidLeaveReason.getReason_name());
            paidLeaveChart.setValue(
                chartRepository.countPaidLeaveReasonByDateAndReasonID(
                    startDate, endDate, paidLeaveReason.getReason_id(), areaName));

            paidLeaveChartList.add(paidLeaveChart);
          });

      chart.setValue(paidLeaveChartList);
      list.add(chart);
    }
    return list;
  }

  @Override
  public List<GeneralSalaryChart> getSalaryStructureByDateAndEmployeeID(
      LocalDate date, String employeeID) {
    List<GeneralSalaryChart> list = new ArrayList<>();
    Map<String, BigDecimal> map = new LinkedHashMap<>();

    LocalDate startDate = date.withDayOfMonth(1);
    LocalDate endDate = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));

    Long salaryID =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeID, startDate, endDate, ESalaryMonthly.APPROVED.name());

    SalaryMonthlyDetailResponse salaryMonthly =
        salaryMonthlyService.getSalaryMonthlyDetailBySalaryMonthlyId(salaryID);

    map.put("Base", salaryMonthly.getBase_salary());
    map.put("Bonus", salaryMonthly.getBonusSalaryResponseList().getTotal());
    map.put("Deduction", salaryMonthly.getDeductionSalaryResponseList().getTotal());
    map.put("Advance", salaryMonthly.getAdvanceSalaryResponseList().getTotal());
    map.put("Tax", salaryMonthly.getEmployeeTaxResponseList().getTotal());
    map.put("Insurance", salaryMonthly.getEmployeeInsuranceResponseList().getTotal());

    map.forEach(
        (k, v) -> {
          GeneralSalaryChart chart = new GeneralSalaryChart();
          chart.setLabel(k);
          chart.setValue(v);
          list.add(chart);
        });

    return list;
  }

  @Override
  public List<GeneralSalaryChart> getSalaryHistoryByDateAndEmployeeIDAndType(
      LocalDate date, String employeeID, String type) {
    List<GeneralSalaryChart> list = new ArrayList<>();
    Map<String, BigDecimal> map = new LinkedHashMap<>();

    LocalDate startDate = date.withDayOfYear(1);
    LocalDate endDate = date.with(lastDayOfYear());
    if ("yearly".equalsIgnoreCase(type)) {
      endDate = LocalDate.now().with(lastDayOfYear());
    }

    List<SalaryMonthlyResponse> monthlyResponses =
        chartRepository.getSalaryHistoryByDateAndEmployeeIDAndType(
            startDate, endDate, employeeID, type);

    AtomicReference<BigDecimal> value = new AtomicReference<>(BigDecimal.ZERO);
    AtomicInteger count = new AtomicInteger();
    AtomicInteger currentYear = new AtomicInteger();
    monthlyResponses.forEach(
        data -> {
          LocalDate date1 = data.getStartDate();
          GeneralSalaryChart chart = new GeneralSalaryChart();
          if ("yearly".equalsIgnoreCase(type)) {
            if (currentYear.get() == 0) {
              value.set(data.getFinalSalary());
              currentYear.set(date1.getYear());
              count.set(1);

              map.put(String.valueOf(currentYear), value.get());
            } else if (currentYear.get() == date1.getYear()) {
              value.set(value.get().add(data.getFinalSalary()));
              currentYear.set(date1.getYear());
              count.getAndIncrement();

              map.put(
                  String.valueOf(currentYear), value.get().divide(BigDecimal.valueOf(count.get())));
            } else if (currentYear.get() != date1.getYear()) {
              map.put(
                  String.valueOf(currentYear), value.get().divide(BigDecimal.valueOf(count.get())));

              value.set(data.getFinalSalary());
              currentYear.set(date1.getYear());
              count.set(1);

              map.put(String.valueOf(currentYear), value.get());
            }
          } else if ("monthly".equalsIgnoreCase(type)) {
            chart.setLabel(
                Month.of(date1.getMonthValue())
                        .getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US)
                    + " "
                    + date1.getYear());
            chart.setValue(data.getFinalSalary());
            list.add(chart);
          }
        });
    if ("yearly".equalsIgnoreCase(type)) {
      map.forEach(
          (k, v) -> {
            GeneralSalaryChart chart = new GeneralSalaryChart();
            chart.setLabel(k);
            chart.setValue(v);
            list.add(chart);
          });
    }

    return list;
  }

  @Override
  public LocalDate getStartDateOfContract(String employeeID) {
    return chartRepository.getStartDateOfContract(employeeID);
  }
}