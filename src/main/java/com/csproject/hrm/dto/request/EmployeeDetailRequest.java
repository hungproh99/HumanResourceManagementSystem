package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeDetailRequest {
  private String employee_id;
  private String personal_email;
  private String company_email;
  private String full_name;
  private String gender;
  private String address;
  private String phone_number;
  private LocalDate birth_date;
  private String marital_status;
  private Boolean working_status;
  private Long role_type;
  private String manager_id;
  private String avatar;
  private String nick_name;
  private String facebook;
  private String tax_code;
  private String current_situation;
  private Long employee_type_id;
  private Long working_type_id;

  private Long working_contract_id;
  private String company_name;
  private Long contract_type_id;
  private LocalDate start_date;
  private LocalDate end_date;
  private BigDecimal base_salary;
  private String contract_url;
  private Boolean contract_status;
  private Long area_id;
  private Long office_id;
  private Long job_id;
}