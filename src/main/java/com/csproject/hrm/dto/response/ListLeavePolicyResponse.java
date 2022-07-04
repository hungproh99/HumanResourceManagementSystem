package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListLeavePolicyResponse {
  private List<LeavePolicyResponse> leavePolicyResponseList;
  private int total;
}