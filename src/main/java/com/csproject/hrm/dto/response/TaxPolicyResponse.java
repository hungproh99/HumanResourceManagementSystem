package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxPolicyResponse {
  private Long tax_policy_type_id;
  private String tax_policy_id;
  private String tax_policy_type;
  private String tax_policy_name;
  private LocalDate created_date;
  private LocalDate effective_date;
  private String description;
  private Boolean tax_policy_status;
  private int percentage;
}