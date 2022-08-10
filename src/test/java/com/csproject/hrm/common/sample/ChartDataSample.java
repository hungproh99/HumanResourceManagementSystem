package com.csproject.hrm.common.sample;

import com.csproject.hrm.dto.chart.*;

import java.util.ArrayList;
import java.util.Arrays;

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
}