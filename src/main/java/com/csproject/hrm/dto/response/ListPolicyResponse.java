package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListPolicyResponse {
  private List<PolicyResponse> policyResponseList;
  private int total;
}