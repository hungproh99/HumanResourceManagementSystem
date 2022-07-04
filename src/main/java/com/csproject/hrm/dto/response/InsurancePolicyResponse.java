package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsurancePolicyResponse {
  private Long insurance_policy_type_id;
  private String insurance_policy_id;
  private String insurance_policy_type;
  private String insurance_policy_name;
  private LocalDate created_date;
  private LocalDate effective_date;
  private String description;
  private Boolean insurance_policy_status;
  private int percentage;
}