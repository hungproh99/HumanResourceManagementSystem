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
  private String working_type;
  private String start_date;
}