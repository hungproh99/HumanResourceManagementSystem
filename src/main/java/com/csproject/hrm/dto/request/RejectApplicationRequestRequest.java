package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RejectApplicationRequestRequest {
  @Positive(message = "requestId must be a positive number!")
  private Long requestId;

  private String comment;
}
