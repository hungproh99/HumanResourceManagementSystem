package com.csproject.hrm.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateApplicationRequestRequest {
  private Long applicationRequestId;
  private String approverId;
  private String requestStatus;
}