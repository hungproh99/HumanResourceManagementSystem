package com.csproject.hrm.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingInfoResponse {
  private String final_salary;
  private String base_salary;
  private String office;
  private String area;
  private String position;
  private String grade;
  private String working_type;
  private String start_date;
  private String employee_type;
  private String manager_id;
  private String manager_name;
  private String salary_contract_id;
  private String working_contract_id;
  private String working_place_id;
  private WorkingInfoResponse newWorkingInfo;
}