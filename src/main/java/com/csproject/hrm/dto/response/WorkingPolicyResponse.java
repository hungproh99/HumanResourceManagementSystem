package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkingPolicyResponse {
  private Long working_policy_type_id;
  private String working_policy_id;
  private String working_policy_type;
  private String working_policy_name;
  private LocalDate created_date;
  private LocalDate effective_date;
  private String description;
  private Boolean working_policy_status;
}