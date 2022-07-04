package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeavePolicyResponse {
  private Long leave_policy_type_id;
  private String leave_policy_id;
  private String leave_policy_type;
  private String leave_policy_name;
  private LocalDate created_date;
  private LocalDate effective_date;
  private String description;
  private Boolean leave_policy_status;
}