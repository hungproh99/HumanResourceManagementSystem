package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateApplicationRequestRequest {
  private Long applicationRequestId;
  private String requestStatus;
  private String approverId;
}
