package com.csproject.hrm.common.sample;

import com.csproject.hrm.common.utils.DateUtils;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

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
          1L);
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

  public static final LoginRequest LOGIN_REQUEST_WRONG_EMAIL =
      new LoginRequest("huynq100", "HUy123456789!");

  public static final LoginRequest LOGIN_REQUEST_NULL_EMAIL =
      new LoginRequest(null, "HUy123456789!");

  public static final LoginRequest LOGIN_REQUEST_WRONG_PASSWORD =
      new LoginRequest("huynq100@fpt.edu.vn", "HUy1234");

  public static final LoginRequest LOGIN_REQUEST_NULL_PASSWORD =
      new LoginRequest("huynq100@fpt.edu.vn", null);

  public static final ChangePasswordRequest CHANGE_PASSWORD_REQUEST =
      new ChangePasswordRequest(
          "huynq100@fpt.edu.vn", "HUy123456789!", "HUy123456789@", "HUy123456789@");

  public static final ChangePasswordRequest CHANGE_PASSWORD_REQUEST_NULL_EMAIL =
      new ChangePasswordRequest(null, "HUy123456789!", "HUy123456789@", "HUy123456789@");

  public static final ChangePasswordRequest CHANGE_PASSWORD_REQUEST_WRONG_EMAIL =
      new ChangePasswordRequest("null", "HUy123456789!", "HUy123456789@", "HUy123456789@");

  public static final ChangePasswordRequest CHANGE_PASSWORD_REQUEST_NULL_PASSWORD =
      new ChangePasswordRequest("huynq100@fpt.edu.vn", null, "HUy123456789@", "HUy123456789@");
  public static final ChangePasswordRequest CHANGE_PASSWORD_REQUEST_WRONG_PASSWORD =
      new ChangePasswordRequest("huynq100@fpt.edu.vn", "null", "HUy123456789@", "HUy123456789@");

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

  public static final HrmRequest HRM_REQUEST =
      new HrmRequest(
          "Nguyen Quang Huy",
          1L,
          "0385822476",
          "Male",
          LocalDate.parse("2000-02-13"),
          1L,
          1L,
          1L,
          1L,
          1L,
          "huynq100",
          1L,
          "huynq100@fpt.edu.vn",
          LocalDate.parse("2000-02-13"),
          LocalDate.parse("2000-02-13"));

  public static final List<WorkingTypeDto> LIST_WORKING_TYPE =
      Arrays.asList(
          new WorkingTypeDto(1L, "FULL_TIME", ""), new WorkingTypeDto(2L, "PART_TIME", ""));

  public static final List<EmployeeTypeDto> LIST_EMPLOYEE_TYPE =
      Arrays.asList(
          new EmployeeTypeDto(1L, "TRAINEE", ""), new EmployeeTypeDto(2L, "OFFICIAL_EMPLOYEE", ""));

  public static final List<AreaDto> LIST_AREA_DTO =
      Arrays.asList(new AreaDto(1L, "BACK_OFFICE", ""), new AreaDto(2L, "SALES", ""));

  public static final List<JobDto> LIST_JOB_DTO =
      Arrays.asList(new JobDto(1L, "IT", ""), new JobDto(2L, "MARKETING", ""));

  public static final List<GradeDto> LIST_GRADE_DTO =
      Arrays.asList(new GradeDto(1L, "DEVELOP_1", ""), new GradeDto(2L, "DEVELOP_2", ""));

  public static final List<OfficeDto> LIST_OFFICE_DTO =
      Arrays.asList(new OfficeDto(1L, "HN_OFFICE", ""), new OfficeDto(2L, "DN_OFFICE", ""));

  public static final List<RoleDto> LIST_ROLE_DTO =
      Arrays.asList(new RoleDto(1L, "ROLE_USER"), new RoleDto(2L, "ROLE_ADMIN"));

  public static final List<String> LIST_EMPLOYEE_ID = Arrays.asList("huynq100", "lienpt1");

  public static final List<EmployeeNameAndID> NAME_AND_ID_LIST =
      Arrays.asList(
          new EmployeeNameAndID("huynq100", "Nguyen Quang Huy"),
          new EmployeeNameAndID("lienpt1", "Pham Thi Lien"));

  public static final ListTimekeepingStatusResponse LIST_TIMEKEEPING_STATUS_RESPONSE =
      new ListTimekeepingStatusResponse(1L, "WORK_LATE");

  public static final TimekeepingResponse TIMEKEEPING_RESPONSE =
      new TimekeepingResponse(
          1L,
          Date.valueOf(LocalDate.now()),
          Arrays.asList(LIST_TIMEKEEPING_STATUS_RESPONSE),
          Time.valueOf(LocalTime.now()),
          Time.valueOf(LocalTime.now()));

  public static final TimekeepingResponsesList TIMEKEEPING_RESPONSES_LIST =
      new TimekeepingResponsesList(
          Arrays.asList(
              new TimekeepingResponses(
                  "huynq100",
                  "Nguyen Quang Huy",
                  "IT",
                  "DEVELOP_1",
                  Arrays.asList(TIMEKEEPING_RESPONSE))),
          1);

  public static final ApplicationsRequestResponse APPLICATIONS_REQUEST_RESPONSE =
      new ApplicationsRequestResponse(
          1L,
          "huynq100",
          "Nguyen Quang Huy",
          LocalDateTime.now(),
          "LEAVE_SOON",
          "WORKING_TIME",
          "Request",
          "APPROVED",
          LocalDateTime.now(),
          LocalDateTime.now(),
          "lienpt1",
          Arrays.asList("hungnq", "lienpt2"),
          "true",
          "true",
          "Request",
          "No comment");

  public static final ListApplicationsRequestResponse LIST_APPLICATIONS_REQUEST_RESPONSE =
      new ListApplicationsRequestResponse(Arrays.asList(APPLICATIONS_REQUEST_RESPONSE), 1);

  public static final UpdateApplicationRequestRequest UPDATE_APPLICATION_REQUEST_REQUEST =
      new UpdateApplicationRequestRequest(1L, "APPROVED", "huynq100");

  public static final RejectApplicationRequestRequest REJECT_APPLICATION_REQUEST_REQUEST =
      new RejectApplicationRequestRequest(1L, "No comment");

  public static final SalaryMonthlyResponse SALARY_MONTHLY_RESPONSE =
      new SalaryMonthlyResponse(
          1L,
          "huynq100",
          "Nguyen Quang Huy",
          "IT",
          "lienpt1",
          0D,
          0D,
          0D,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          LocalDate.now(),
          LocalDate.now(),
          "APPROVED",
          "No comment");

  public static final SalaryMonthlyResponseList SALARY_MONTHLY_RESPONSE_LIST =
      new SalaryMonthlyResponseList(Arrays.asList(SALARY_MONTHLY_RESPONSE), 1, "true");

  public static final PointResponse POINT_RESPONSE = new PointResponse(0D, 0D, 0D, 0D);
  public static final OTDetailResponse OT_DETAIL_RESPONSE =
      new OTDetailResponse(1L, LocalDate.now(), LocalTime.now(), LocalTime.now(), 0D);
  public static final OTResponse OT_RESPONSE =
      new OTResponse("IN_WEEK", Arrays.asList(OT_DETAIL_RESPONSE), 0D);
  public static final OTResponseList OT_RESPONSE_LIST =
      new OTResponseList(Arrays.asList(OT_RESPONSE), 0D);

  public static final BonusSalaryResponse BONUS_SALARY_RESPONSE =
      new BonusSalaryResponse(
          1L, BigDecimal.ZERO, "PROJECT_BONUS", Date.valueOf(LocalDate.now()), "");

  public static final BonusSalaryResponseList BONUS_SALARY_RESPONSE_LIST =
      new BonusSalaryResponseList(Arrays.asList(BONUS_SALARY_RESPONSE), BigDecimal.ZERO);

  public static final DeductionSalaryResponse DEDUCTION_SALARY_RESPONSE =
      new DeductionSalaryResponse(
          1L, BigDecimal.ZERO, "LATE_WORK", Date.valueOf(LocalDate.now()), "");

  public static final DeductionSalaryResponseList DEDUCTION_SALARY_RESPONSE_LIST =
      new DeductionSalaryResponseList(Arrays.asList(DEDUCTION_SALARY_RESPONSE), BigDecimal.ZERO);
  public static final AdvanceSalaryResponse ADVANCE_SALARY_RESPONSE =
      new AdvanceSalaryResponse(1L, BigDecimal.ZERO, Date.valueOf(LocalDate.now()), "");

  public static final AdvanceSalaryResponseList ADVANCE_SALARY_RESPONSE_LIST =
      new AdvanceSalaryResponseList(Arrays.asList(ADVANCE_SALARY_RESPONSE), BigDecimal.ZERO);
  public static final EmployeeAllowanceResponse EMPLOYEE_ALLOWANCE_RESPONSE =
      new EmployeeAllowanceResponse(1L, BigDecimal.ZERO, "ALLOWANCE", "TRANSPORTATION_ALLOWANCE");

  public static final EmployeeAllowanceResponseList EMPLOYEE_ALLOWANCE_RESPONSE_LIST =
      new EmployeeAllowanceResponseList(
          Arrays.asList(EMPLOYEE_ALLOWANCE_RESPONSE), BigDecimal.ZERO);
  public static final EmployeeTaxResponse EMPLOYEE_TAX_RESPONSE =
      new EmployeeTaxResponse(1L, BigDecimal.ZERO, "TAX", "VNP", 0D);

  public static final EmployeeTaxResponseList EMPLOYEE_TAX_RESPONSE_LIST =
      new EmployeeTaxResponseList(Arrays.asList(EMPLOYEE_TAX_RESPONSE), BigDecimal.ZERO);
  public static final EmployeeInsuranceResponse EMPLOYEE_INSURANCE_RESPONSE =
      new EmployeeInsuranceResponse(1L, BigDecimal.ZERO, "INSURANCE", "SI", 0D);

  public static final EmployeeInsuranceResponseList EMPLOYEE_INSURANCE_RESPONSE_LIST =
      new EmployeeInsuranceResponseList(
          Arrays.asList(EMPLOYEE_INSURANCE_RESPONSE), BigDecimal.ZERO);

  public static final SalaryMonthlyDetailResponse SALARY_MONTHLY_DETAIL_RESPONSE =
      new SalaryMonthlyDetailResponse(
          1L,
          "huynq100",
          "Nguyen Quang Huy",
          "IT",
          "lienpt1",
          LocalDate.now(),
          LocalDate.now(),
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          0D,
          "APPROVED",
          POINT_RESPONSE,
          OT_RESPONSE_LIST,
          BONUS_SALARY_RESPONSE_LIST,
          DEDUCTION_SALARY_RESPONSE_LIST,
          ADVANCE_SALARY_RESPONSE_LIST,
          EMPLOYEE_ALLOWANCE_RESPONSE_LIST,
          EMPLOYEE_TAX_RESPONSE_LIST,
          EMPLOYEE_INSURANCE_RESPONSE_LIST);

  public static final DeductionSalaryRequest DEDUCTION_SALARY_REQUEST =
      new DeductionSalaryRequest(1L, BigDecimal.ONE, "Description", LocalDate.now(), 1L);

  public static final BonusSalaryRequest BONUS_SALARY_REQUEST =
      new BonusSalaryRequest(1L, BigDecimal.ONE, "Description", LocalDate.now(), 1L);
  public static final AdvanceSalaryRequest ADVANCE_SALARY_REQUEST =
      new AdvanceSalaryRequest(1L, BigDecimal.ONE, "Description", LocalDate.now());

  public static final RejectSalaryMonthlyRequest REJECT_SALARY_MONTHLY_REQUEST =
      new RejectSalaryMonthlyRequest(1L, "No comment");
  public static final UpdateSalaryMonthlyRequest UPDATE_SALARY_MONTHLY_REQUEST =
      new UpdateSalaryMonthlyRequest(1L, "APPROVE", "huynq100");

  public static final List<DeductionTypeDto> DEDUCTION_TYPE_DTO_LIST =
      Arrays.asList(new DeductionTypeDto(1L, "WORK_LATE"));

  public static final List<BonusTypeDto> BONUS_TYPE_DTO_LIST =
      Arrays.asList(new BonusTypeDto(1L, "PROJECT_BONUS"));
}