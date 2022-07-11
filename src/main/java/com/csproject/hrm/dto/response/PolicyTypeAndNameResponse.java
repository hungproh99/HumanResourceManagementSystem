package com.csproject.hrm.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PolicyTypeAndNameResponse {
  private String policy_type;
  private String policy_name;
}