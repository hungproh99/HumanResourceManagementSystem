package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaidLeaveReasonDto {
  private Long reason_id;
  private String reason_name;
}