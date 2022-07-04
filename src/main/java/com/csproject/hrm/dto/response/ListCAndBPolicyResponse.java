package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCAndBPolicyResponse {
  private List<CAndBPolicyResponse> cAndBPolicyResponseList;
  private int total;
}