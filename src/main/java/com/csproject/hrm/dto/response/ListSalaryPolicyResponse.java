package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListSalaryPolicyResponse {
  private List<SalaryPolicyResponse> salaryPolicyResponseList;
  private int total;
}