package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListWorkingPolicyResponse {
  private List<WorkingPolicyResponse> workingPolicyResponseList;
  private int total;
}