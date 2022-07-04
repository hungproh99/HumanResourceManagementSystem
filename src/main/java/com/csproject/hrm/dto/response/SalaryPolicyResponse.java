package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryPolicyResponse {
  private Long salary_policy_type_id;
  private String salary_policy_id;
  private String salary_policy_type;
  private String salary_policy_name;
  private LocalDate created_date;
  private LocalDate effective_date;
  private String description;
  private Boolean salary_policy_status;
}