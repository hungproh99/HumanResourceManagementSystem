package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointResponse {
  private Double actual_working_day;
  private Double unpaid_leave_day;
  private Double paid_leave_day;
}
