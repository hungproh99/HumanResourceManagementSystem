package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListTaxPolicyResponse {
  private List<TaxPolicyResponse> taxPolicyResponseList;
  private int total;
}