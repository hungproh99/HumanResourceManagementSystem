package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.chart.*;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.ChartService;
import com.csproject.hrm.services.SalaryMonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.csproject.hrm.common.constant.Constants.BEARER;
import static com.csproject.hrm.common.constant.Constants.NO_EMPLOYEE_WITH_ID;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class ChartServiceImpl implements ChartService {

  @Autowired ChartRepository chartRepository;
  @Autowired WorkingPlaceRepository contractRepository;
  @Autowired SalaryMonthlyRepository salaryMonthlyRepository;
  @Autowired SalaryMonthlyService salaryMonthlyService;
  @Autowired EmployeeDetailRepository employeeDetailRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired JwtUtils jwtUtils;

  @Override
  public String getAreaNameByEmployeeID(String employeeID) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
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
              getEmployeeByManagerID(
                  employee.getEmployeeID(), EArea.getLabel(area.getName()), area.getArea_id()));
          list.add(employee);
        });
    return list;
  }

  private List<EmployeeChart> getEmployeeByManagerID(
      String managerID, String areaName, long areaID) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(managerID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + managerID);
    }
    List<EmployeeChart> list = chartRepository.getEmployeeByManagerIDAndAreaID(managerID, areaID);
    AtomicInteger i = new AtomicInteger(1);

    list.forEach(
        employee -> {
          employee.setTitle(areaName + "-" + i.getAndIncrement());
          employee.setChildren(
              getEmployeeByManagerID(employee.getEmployeeID(), employee.getTitle(), areaID));
        });
    return list;
  }

  @Override
  public List<LeaveCompanyChart> getLeaveCompanyReasonByYearAndAreaName(
      Integer year, String areaName) {
    List<LeaveCompanyChart> list = new ArrayList<>();
    List<LeaveCompanyReasonDto> LeaveCompanyReasonList = chartRepository.getAllLeaveCompanyReason();

    for (int i = 0; i <= 3; i++) {
      LeaveCompanyChart chart = new LeaveCompanyChart();
      chart.setLabel("Q" + (i + 1) + " " + year);

      List<LeaveCompanyChartList> leaveCompanyChartList = new ArrayList<>();
      int finalI = i * 3;
      LeaveCompanyReasonList.forEach(
          leaveCompanyReason -> {
            Month month = Month.of(finalI + 3);
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
  public List<PaidLeaveChart> getPaidLeaveReasonByYearAndManagerID(
      String headerAuth, Integer year, String employeeId) {
    if (employeeId == null) {
      throw new NullPointerException("Param employeeId is null!");
    }
    List<PaidLeaveChart> list = new ArrayList<>();

    List<PaidLeaveReasonDto> paidLeaveReasonList = chartRepository.getAllPaidLeaveReason();
    for (int i = 1; i <= 12; i++) {
      PaidLeaveChart chart = new PaidLeaveChart();
      chart.setLabel(
          Month.of(i).getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US) + " " + year);

      List<PaidLeaveChartList> paidLeaveChartList = new ArrayList<>();

      for (PaidLeaveReasonDto paidLeaveReason : paidLeaveReasonList) {
        LocalDate startDate = LocalDate.of(year, i, 1);
        LocalDate endDate =
            LocalDate.of(year, i, startDate.getMonth().length(startDate.isLeapYear()));

        PaidLeaveChartList paidLeaveChart = new PaidLeaveChartList();

        paidLeaveChart.setReason(paidLeaveReason.getReason_name());
        int value = 0;
        if ("".equals(employeeId)) {
          if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            String jwt = headerAuth.substring(7);
            employeeId = jwtUtils.getIdFromJwtToken(jwt);
          }
          List<EmployeeNameAndID> employeeList =
              employeeDetailRepository.getAllEmployeeByManagerID(employeeId);
          for (EmployeeNameAndID employee : employeeList) {
            value +=
                chartRepository.countPaidLeaveReasonByDateAndReasonID(
                    startDate, endDate, paidLeaveReason.getReason_id(), employee.getEmployeeID());
          }
          employeeId = "";
        } else {
          if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
            throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
          }
          value =
              chartRepository.countPaidLeaveReasonByDateAndReasonID(
                  startDate, endDate, paidLeaveReason.getReason_id(), employeeId);
        }
        paidLeaveChart.setValue(value);

        paidLeaveChartList.add(paidLeaveChart);
      }

      chart.setValue(paidLeaveChartList);
      list.add(chart);
    }
    return list;
  }

  @Override
  public List<GeneralSalaryChart> getSalaryStructureByDateAndEmployeeID(
      LocalDate date, String employeeID) {

    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }

    List<GeneralSalaryChart> list = new ArrayList<>();
    Map<String, BigDecimal> map = new LinkedHashMap<>();

    LocalDate startDate = date.withDayOfMonth(1);
    LocalDate endDate = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(startDate, endDate, employeeID);
    actualWorkingPoint = actualWorkingPoint != null ? actualWorkingPoint : 0D;
    Long salaryID =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeID, startDate, endDate, actualWorkingPoint, ESalaryMonthly.APPROVED.name());

    SalaryMonthlyDetailResponse salaryMonthly =
        salaryMonthlyService.getSalaryMonthlyDetailBySalaryMonthlyId(salaryID);

    map.put("Base", Objects.requireNonNullElse(salaryMonthly.getBase_salary(), BigDecimal.ZERO));
    map.put(
        "Bonus",
        Objects.requireNonNullElse(
            salaryMonthly.getBonusSalaryResponseList().getTotal(), BigDecimal.ZERO));
    map.put(
        "Deduction",
        Objects.requireNonNullElse(
            salaryMonthly.getDeductionSalaryResponseList().getTotal(), BigDecimal.ZERO));
    map.put(
        "Advance",
        Objects.requireNonNullElse(
            salaryMonthly.getAdvanceSalaryResponseList().getTotal(), BigDecimal.ZERO));
    map.put(
        "Tax",
        Objects.requireNonNullElse(
            salaryMonthly.getEmployeeTaxResponseList().getTotal(), BigDecimal.ZERO));
    map.put(
        "Insurance",
        Objects.requireNonNullElse(
            salaryMonthly.getEmployeeInsuranceResponseList().getTotal(), BigDecimal.ZERO));

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

    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }

    List<GeneralSalaryChart> list = new ArrayList<>();
    Map<String, BigDecimal> map = new LinkedHashMap<>();

    LocalDate startDate = date.withDayOfYear(1);
    LocalDate endDate = date.with(lastDayOfYear());
    if ("yearly".equalsIgnoreCase(type)) {
      endDate = Instant.now().atZone(ZoneId.of("UTC+07")).toLocalDate().with(lastDayOfYear());
    } else if ("monthly".equalsIgnoreCase(type)) {
      String year = String.valueOf(startDate.getYear());
      map.put("Jan " + year, BigDecimal.ZERO);
      map.put("Feb " + year, BigDecimal.ZERO);
      map.put("Mar " + year, BigDecimal.ZERO);
      map.put("Apr " + year, BigDecimal.ZERO);
      map.put("May " + year, BigDecimal.ZERO);
      map.put("Jun " + year, BigDecimal.ZERO);
      map.put("Jul " + year, BigDecimal.ZERO);
      map.put("Aug " + year, BigDecimal.ZERO);
      map.put("Sep " + year, BigDecimal.ZERO);
      map.put("Oct " + year, BigDecimal.ZERO);
      map.put("Nov " + year, BigDecimal.ZERO);
      map.put("Dec " + year, BigDecimal.ZERO);
    } else {
      throw new CustomErrorException("Type is not valid!");
    }
    List<SalaryMonthlyResponse> monthlyResponses =
        chartRepository.getSalaryHistoryByDateAndEmployeeIDAndType(
            startDate, endDate, employeeID, type);
    BigDecimal value = BigDecimal.ZERO;
    int count = 0;
    int currentYear = 0;
    for (SalaryMonthlyResponse data : monthlyResponses) {
      LocalDate date1 = data.getStartDate();
      if ("yearly".equalsIgnoreCase(type)) {
        if (currentYear == 0) {
          value = (data.getFinalSalary());
          currentYear = (date1.getYear());
          count = (1);

          map.put(String.valueOf(currentYear), value);
        } else if (currentYear == date1.getYear()) {
          value = (value.add(data.getFinalSalary()));
          currentYear = (date1.getYear());
          count++;

          map.put(
              String.valueOf(currentYear),
              value.divide(BigDecimal.valueOf(count), RoundingMode.HALF_DOWN));
        } else {
          map.put(
              String.valueOf(currentYear),
              value.divide(BigDecimal.valueOf(count), RoundingMode.HALF_DOWN));

          value = (data.getFinalSalary());
          currentYear = (date1.getYear());
          count = (1);

          map.put(String.valueOf(currentYear), value);
        }
      } else if ("monthly".equalsIgnoreCase(type)) {
        String label =
            Month.of(date1.getMonthValue()).getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US)
                + " "
                + date1.getYear();
        map.put(label, map.get(label).add(data.getFinalSalary()));
      }
    }

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
  public LocalDate getStartDateOfContract(String employeeID) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    return chartRepository.getStartDateOfContract(employeeID);
  }

  @Override
  public List<PaidLeaveReasonDto> getAllPaidLeaveReason() {
    List<PaidLeaveReasonDto> list = chartRepository.getAllPaidLeaveReason();
    list.forEach(
        reason -> reason.setReason_name(EPaidLeaveReason.getLabel(reason.getReason_name())));
    return list;
  }
}