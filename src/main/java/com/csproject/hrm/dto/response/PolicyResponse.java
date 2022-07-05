package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PolicyResponse {
  private Long policy_type_id;
  private String policy_id;
  private String policy_type;
  private String policy_name;
  private LocalDate created_date;
  private LocalDate effective_date;
  private String description;
  private Boolean policy_status;
  private String policy_category;
}