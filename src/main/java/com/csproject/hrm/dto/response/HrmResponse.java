package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrmResponse {
  private String employee_id;
  private String full_name;
  private String email;
  private String working_status;
  private String phone;
  private String gender;
  private Date birth_date;
  private String grade;
  private String office_name;
  private String area_name;
  private String seniority;
  private String position_name;
  private String working_name;
}
