package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListInsurancePolicyResponse {
  private List<InsurancePolicyResponse> insurancePolicyResponseList;
  private int total;
}