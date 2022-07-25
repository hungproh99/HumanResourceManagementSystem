package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeDetailRequest {
  private String full_name;
  private String employee_id;
  private LocalDate start_date;
  private LocalDate end_date;
  private Boolean working_status;
  private String contract_url;
  private String phone_number;
  private Long grade_id;
  private LocalDate birth_date;
  private String company_email;
  private String personal_email;
  private String gender;
  private String marital_status;
  private Long office_id;
  private Long job_id;
  private Long area_id;
  private Long working_contract_id;
  private Long working_place_id;
  private String avatar;
}