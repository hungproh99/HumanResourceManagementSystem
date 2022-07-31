package com.csproject.hrm.common.sample;

import com.csproject.hrm.common.utils.DateUtils;
import com.csproject.hrm.dto.dto.EmployeeInsuranceDto;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;

import java.util.Arrays;

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
  public static final String EMPLOYEE_DETAIL_RESPONSE_JSON =
      "{\"full_name\":\"Nguyen Quang Huy\",\"employee_id\":\"huynq100\",\"company_email\":\"huynq100@fpt.edu.vn\","
          + "\"seniority\":\"3 year 7 month 26 day \",\"start_date\":[2019,1,2],\"working_status\":\"Active\","
          + "\"contract_url\":\"url\",\"phone_number\":\"0912345678\",\"grade\":\"Marketing 2\","
          + "\"birth_date\":[2000,12,8],\"gender\":\"Male\",\"marital_status\":\"Alone\","
          + "\"office_name\":\"Hà Nội Office\",\"position_name\":\"Marketing\",\"area_name\":\"Back Office\","
          + "\"avatar\":\"https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg\","
          + "\"working_name\":\"Full Time\",\"working_contract_id\":1,\"working_place_id\":1}";
  public static final String TAX_AND_INSURANCE_RESPONSE_JSON =
      "{\"tax_code\":null,\"insuranceDtos\":[{\"insuranceID\":1,\"insuranceName\":\"HI\",\"address\":null},"
          + "{\"insuranceID\":2,\"insuranceName\":\"SI\",\"address\":null},{\"insuranceID\":3,"
          + "\"insuranceName\":\"UI\",\"address\":null},{\"insuranceID\":4,\"insuranceName\":\"PI\","
          + "\"address\":null}]}";
  public static final String EMPLOYEE_ADDITIONAL_INFO_RESPONSE_JSON =
      "{\"address\":null,\"place_of_residence\":\"Thạch Thất, Hà Nội\",\"place_of_origin\":\"Thạch Thất, Hà Nội\","
          + "\"nationality\":\"Viet Nam\",\"card_id\":\"03251837462\",\"provideDate\":\"2021-09-01\","
          + "\"providePlace\":\"Thạch Thất, Hà Nội\",\"personal_email\":null,\"phone_number\":\"0912345678\","
          + "\"nick_name\":null,\"facebook\":null}";
}