package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponse {
  private String full_name;
  private String employee_id;
  private String company_email;
  private LocalDate seniority;
  private LocalDate start_date;
  private String working_status;
  private String contract_url;
  private String phone_number;
  private String grade;
  private LocalDate birth_date;
  private String gender;
  private String marital_status;
  private String office_name;
  private String position_name;
  private String area_name;
  private String avatar;
  private String working_name;
  private Long working_contract_id;
}