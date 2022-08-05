package com.csproject.hrm.common.sample;

import com.csproject.hrm.common.utils.DateUtils;
import com.csproject.hrm.dto.dto.EmployeeInsuranceDto;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;

import java.sql.Date;
import java.util.Arrays;

import static com.csproject.hrm.common.enums.ERole.ROLE_MANAGER;

public class DataSample {
  public static final EmployeeDetailResponse DETAIL_RESPONSE =
      new EmployeeDetailResponse(
          "Nguyen Quang Huy",
          "huynq100",
          "huynq100@fpt.edu.vn",
          "3 year 7 month 26 day ",
          DateUtils.convert("2019-01-02"),
          DateUtils.convert("2025-01-01"),
          "Active",
          "url",
          "0912345678",
          "Marketing 2",
          DateUtils.convert("2000-12-08"),
          "Male",
          "Alone",
          "Hà Nội Office",
          "Marketing",
          "Back Office",
          "https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg",
          "Full Time",
          1L,
          1L);
  public static final TaxAndInsuranceResponse TAX_AND_INSURANCE_RESPONSE =
      new TaxAndInsuranceResponse(
          null,
          Arrays.asList(
              new EmployeeInsuranceDto(1L, "HI", null),
              new EmployeeInsuranceDto(2L, "SI", null),
              new EmployeeInsuranceDto(3L, "UI", null),
              new EmployeeInsuranceDto(4L, "PI", null)));
  public static final EmployeeAdditionalInfo EMPLOYEE_ADDITIONAL_INFO =
      new EmployeeAdditionalInfo(
          null,
          "Thạch Thất, Hà Nội",
          "Thạch Thất, Hà Nội",
          "Viet Nam",
          "03251837462",
          "2021-09-01",
          "Thạch Thất, Hà Nội",
          null,
          "0912345678",
          null,
          null);
  public static final BankResponse BANK_RESPONSE =
      new BankResponse(1L, "TPBank", "Thạch Thất, Hà Nội", "2341122134", "NGUYEN THI AN");
  public static final EmployeeDetailRequest EMPLOYEE_DETAIL_REQUEST =
      new EmployeeDetailRequest(
          "Nguyen Quang Huy",
          "huynq100",
          DateUtils.convert("2019-01-02"),
          DateUtils.convert("2025-01-01"),
          true,
          "url",
          "0912345678",
          4L,
          DateUtils.convert("2000-12-08"),
          "huynq100@fpt.edu.vn",
          "Male",
          "Alone",
          1L,
          2L,
          1L,
          1L,
          1L,
          "https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg");
  public static final EmployeeAdditionalInfoRequest EMPLOYEE_ADDITIONAL_INFO_REQUEST =
      new EmployeeAdditionalInfoRequest(
          "Thạch Thất, Hà Nội",
          "Thạch Thất, Hà Nội",
          "Thạch Thất, Hà Nội",
          "Viet Nam",
          "03251837462",
          DateUtils.convert("2021-09-01"),
          "Thạch Thất, Hà Nội",
          null,
          "0912345678",
          null,
          null,
          null);
  public static final BankRequest BANK_REQUEST =
      new BankRequest(
          1L, "TPBank", "Thạch Thất, Hà Nội", "2341122134", "NGUYEN THI AN", "huynq100");
  public static final EducationRequest EDUCATION_REQUEST =
      new EducationRequest(
          1L,
          "FPT University",
          DateUtils.convert("2017-09-01"),
          DateUtils.convert("2020-09-01"),
          "",
          "End",
          "huynq100");
  public static final ApplicationsRequestCreateRequest APPLICATIONS_REQUEST_TIMEKEEPING =
      new ApplicationsRequestCreateRequest(
          null,
          "lienpt1",
          1L,
          1L,
          null,
          "test",
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          DateUtils.convert("2022-09-12"),
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null);

  public static final LoginRequest LOGIN_REQUEST =
      new LoginRequest("huynq100@fpt.edu.vn", "HUy123456789!");

  public static final ChangePasswordRequest CHANGE_PASSWORD_REQUEST =
      new ChangePasswordRequest(
          "huynq100@fpt.edu.vn", "HUy123456789!", "HUy123456789@", "HUy123456789@");

  public static final JwtResponse JWT_RESPONSE =
      new JwtResponse(
          "huynq100",
          "huynq100@fpt.edu.vn",
          "Nguyen Quang Huy",
          Arrays.asList(ROLE_MANAGER.name()),
          "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NTk3MTkyMjMsImV4cCI6MTY1OTgwNTYyM30.vPn7J72tTFwpJ0cKtSbLYpuwM41ZdIo5z6pjMpkktU7BN7EMHpCarEhRs-rjC4XJUzq9sRW6lTH_OxQQF5M0Vg");

  public static final HrmResponseList HRM_RESPONSE_LIST =
      new HrmResponseList(
          Arrays.asList(
              new HrmResponse(
                  "HungPV1",
                  "Phạm Văn Hùng",
                  "HungPV1@fpt.edu.vn",
                  "Active",
                  "0385822476",
                  "Female",
                  Date.valueOf("2000-02-13"),
                  "Develop 1",
                  "Hồ Chí Minh Office",
                  "Back Office",
                  "2 year 8 month 24 day ",
                  "IT",
                  "Full Time")),
          1);
}
