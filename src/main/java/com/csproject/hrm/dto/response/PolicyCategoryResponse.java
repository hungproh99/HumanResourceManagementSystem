package com.csproject.hrm.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PolicyCategoryResponse {
  private Long policy_category_id;
  private String policy_category;
}