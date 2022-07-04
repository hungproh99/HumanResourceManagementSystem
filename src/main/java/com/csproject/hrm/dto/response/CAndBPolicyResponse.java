package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CAndBPolicyResponse {
  private Long compensation_benefit_policy_type_id;
  private String compensation_benefit_id;
  private String compensation_benefit_policy_type;
  private String compensation_benefit_policy_name;
  private LocalDate created_date;
  private LocalDate effective_date;
  private String description;
  private Boolean compensation_benefit_status;
}