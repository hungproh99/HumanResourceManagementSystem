package com.csproject.hrm.common.sample;

import com.csproject.hrm.common.utils.DateUtils;
import com.csproject.hrm.dto.chart.*;
import com.csproject.hrm.dto.response.EmployeeNameAndID;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class ChartDataSample {
  public static final GeneralDataCharts GENERAL_DATA_CHARTS =
      new GeneralDataCharts(
          9,
          6,
          3,
          new ArrayList<>(
              Arrays.asList(
                  new GeneralChart("<1", 4), new GeneralChart("1-2", 0),
                  new GeneralChart("2-3", 3), new GeneralChart("3-4", 1),
                  new GeneralChart("4-5", 0), new GeneralChart(">5", 1))),
          new ArrayList<>(
              Arrays.asList(
                  new GeneralChart("18-24", 8), new GeneralChart("25-35", 0),
                  new GeneralChart("36-45", 0), new GeneralChart("46-55", 0),
                  new GeneralChart("56-65", 0), new GeneralChart(">66", 1))),
          new ArrayList<>(
              Arrays.asList(
                  new GeneralChart("Full Time", 7),
                  new GeneralChart("Part Time", 2),
                  new GeneralChart("Freelance", 0))));
  public static final OrganizationalChart ORGANIZATIONAL_CHART =
      new OrganizationalChart(
          "HRM",
          new ArrayList<>(
              Arrays.asList(
                  new EmployeeChart(
                      "huynq100",
                      "Back Office",
                      "Nguyen Quang Huy",
                      "https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg",
                      new ArrayList<>(
                          Arrays.asList(
                              new EmployeeChart(
                                  "HungPV1",
                                  "Back Office-1",
                                  "Phạm Văn Hùng",
                                  "https://res.cloudinary.com/pingdaily/image/upload/v1659942929/hrm-avatar/wnuopsqvwsaq8ikupigk.png",
                                  new ArrayList<>(
                                      Arrays.asList(
                                          new EmployeeChart(
                                              "HoangNK1",
                                              "Back Office-1-1",
                                              "Nguyễn Khắc Hoàng",
                                              "https://res.cloudinary.com/pingdaily/image/upload/v1660121288/hrm-avatar/snbcejy7ujbhwyqyyxq1.jpg",
                                              new ArrayList<>()),
                                          new EmployeeChart(
                                              "HoangPQ1",
                                              "Back Office-1-2",
                                              "Phạm Quốc Hoàng",
                                              "https://res.cloudinary.com/pingdaily/image/upload/v1660121471/hrm-avatar/othx7j1fntur4gi9rahn.jpg",
                                              new ArrayList<>())))),
                              new EmployeeChart(
                                  "LienPT2",
                                  "Back Office-2",
                                  "Phạm Thị Liên",
                                  "https://res.cloudinary.com/pingdaily/image/upload/v1660020683/hrm-avatar/nesgf9lrw0yabkmaapm7.jpg",
                                  new ArrayList<>(
                                      Arrays.asList(
                                          new EmployeeChart(
                                              "anv1",
                                              "Back Office-2-1",
                                              "nguyen van a",
                                              null,
                                              new ArrayList<>()),
                                          new EmployeeChart(
                                              "KhangTT1",
                                              "Back Office-2-2",
                                              "Truong Tuan Khang",
                                              "https://res.cloudinary.com/pingdaily/image/upload/v1660037990/hrm-avatar/o4qa5c5cttuoii3mjokk.jpg",
                                              new ArrayList<>()),
                                          new EmployeeChart(
                                              "KhangTT2",
                                              "Back Office-2-3",
                                              "Tran Tuan Khang",
                                              "https://res.cloudinary.com/pingdaily/image/upload/v1660037889/hrm-avatar/yirzobyvntgrb5sukcys.png",
                                              new ArrayList<>()))))))),
                  new EmployeeChart(
                      "LienPT1",
                      "Sales",
                      "Phạm Thị Liên",
                      "https://res.cloudinary.com/pingdaily/image/upload/v1660027532/hrm-avatar/pngknvtd1sty7xf7h1yw.jpg",
                      new ArrayList<>(
                          Arrays.asList(
                              new EmployeeChart(
                                  "huynqhe141565",
                                  "Sales-1",
                                  "Nguyen Quang Huy ",
                                  null,
                                  new ArrayList<>())))),
                  new EmployeeChart(
                      "huynq100",
                      "Marketing",
                      "Nguyen Quang Huy",
                      "https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg",
                      new ArrayList<>()),
                  new EmployeeChart(
                      "huynq100",
                      "Accountant",
                      "Nguyen Quang Huy",
                      "https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg",
                      new ArrayList<>()),
                  new EmployeeChart(
                      "huynq100",
                      "HR",
                      "Nguyen Quang Huy",
                      "https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg",
                      new ArrayList<>()))));
  public static final List<LeaveCompanyChart> LEAVE_COMPANY_CHARTS =
      Arrays.asList(
          new LeaveCompanyChart(
              "Q1 2022",
              Arrays.asList(
                  new LeaveCompanyChartList("Sa thải", 1),
                  new LeaveCompanyChartList("Chuyển công ty", 1),
                  new LeaveCompanyChartList("Khác", 0))),
          new LeaveCompanyChart(
              "Q2 2022",
              Arrays.asList(
                  new LeaveCompanyChartList("Sa thải", 0),
                  new LeaveCompanyChartList("Chuyển công ty", 0),
                  new LeaveCompanyChartList("Khác", 0))),
          new LeaveCompanyChart(
              "Q3 2022",
              Arrays.asList(
                  new LeaveCompanyChartList("Sa thải", 0),
                  new LeaveCompanyChartList("Chuyển công ty", 0),
                  new LeaveCompanyChartList("Khác", 0))),
          new LeaveCompanyChart(
              "Q4 2022",
              Arrays.asList(
                  new LeaveCompanyChartList("Sa thải", 0),
                  new LeaveCompanyChartList("Chuyển công ty", 0),
                  new LeaveCompanyChartList("Khác", 0))));
  public static final List<PaidLeaveChart> PAID_LEAVE_CHARTS =
      Arrays.asList(
          new PaidLeaveChart(
              "Jan 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Feb 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Mar 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Apr 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "May 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Jun 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Jul 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 1))),
          new PaidLeaveChart(
              "Aug 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 1),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Sep 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Oct 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Nov 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))),
          new PaidLeaveChart(
              "Dec 2022",
              Arrays.asList(
                  new PaidLeaveChartList("Family Reason", 0),
                  new PaidLeaveChartList("Unexpected Reason", 0),
                  new PaidLeaveChartList("Health Reason", 0),
                  new PaidLeaveChartList("Other", 0))));
  public static final List<GeneralSalaryChart> SALARY_STRUCTURE =
      Arrays.asList(
          new GeneralSalaryChart("Base", BigDecimal.valueOf(5000000)),
          new GeneralSalaryChart("Bonus", BigDecimal.valueOf(200000)),
          new GeneralSalaryChart("Deduction", BigDecimal.valueOf(200000)),
          new GeneralSalaryChart("Advance", BigDecimal.valueOf(160000)),
          new GeneralSalaryChart("Tax", BigDecimal.valueOf(2250000)),
          new GeneralSalaryChart("Insurance", BigDecimal.valueOf(1680000)));
  public static final List<GeneralSalaryChart> SALARY_HISTORY_YEARLY =
      Arrays.asList(
          new GeneralSalaryChart("2021", BigDecimal.valueOf(3733383.33)),
          new GeneralSalaryChart("2022", BigDecimal.valueOf(3771450)));
  public static final List<GeneralSalaryChart> SALARY_HISTORY_MONTHLY =
      Arrays.asList(
          new GeneralSalaryChart("Jan 2021", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("Feb 2022", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("Mar 2022", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("Apr 2022", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("May 2022", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("Jun 2022", BigDecimal.valueOf(9000050)),
          new GeneralSalaryChart("Jul 2022", BigDecimal.valueOf(8300050)),
          new GeneralSalaryChart("Aug 2022", BigDecimal.valueOf(9100050)),
          new GeneralSalaryChart("Sep 2022", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("Oct 2022", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("Nov 2022", BigDecimal.valueOf(0)),
          new GeneralSalaryChart("Dec 2022", BigDecimal.valueOf(0)));
  public static final List<EmployeeNameAndID> EMPLOYEE_NAME_AND_IDS =
      Arrays.asList(
          new EmployeeNameAndID("HungPV1", "Phạm Văn Hùng"),
              new EmployeeNameAndID("LienPT2", "Phạm Thị Liên"),
          new EmployeeNameAndID("LienPT24", "Phạm Thị Liên"),
              new EmployeeNameAndID("HoangNK1", "Nguyễn Khắc Hoàng"),
          new EmployeeNameAndID("HoangPQ1", "Phạm Quốc Hoàng"),
              new EmployeeNameAndID("anv1", "nguyen van a"),
          new EmployeeNameAndID("KhangTT1", "Truong Tuan Khang"),
              new EmployeeNameAndID("KhangTT2", "Tran Tuan Khang"));
  public static final List<LocalDate> DATE_LIST =
      Arrays.asList(
          DateUtils.convert("2022-02-01"),
          DateUtils.convert("2022-02-02"),
          DateUtils.convert("2022-02-03"),
          DateUtils.convert("2022-02-04"),
          DateUtils.convert("2022-02-05"),
          DateUtils.convert("2022-02-06"),
          DateUtils.convert("2022-04-09"),
          DateUtils.convert("2022-04-10"),
          DateUtils.convert("2022-04-11"),
          DateUtils.convert("2022-04-30"),
          DateUtils.convert("2022-05-01"),
          DateUtils.convert("2022-05-02"),
          DateUtils.convert("2022-05-03"),
          DateUtils.convert("2022-09-01"),
          DateUtils.convert("2022-09-02"),
          DateUtils.convert("2022-09-03"),
          DateUtils.convert("2022-09-04"));
}