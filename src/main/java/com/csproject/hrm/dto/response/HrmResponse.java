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
  private String work_status;
  private String phone;
  private String gender;
  private Date birth_date;
  private String job;
  private String office;
  private String area;
  private String contract;
  private String seniority;
  private Date start_date;
}
