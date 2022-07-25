package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveCompanyReasonDto {
  private Long reason_id;
  private String reason_name;
}