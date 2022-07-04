package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimekeepingDetailResponse {
  private String employee_id;
  private LocalDate current_date;
  private Long timekeeping_id;
  private List<ListTimekeepingStatusResponse> timekeeping_status;
  private String total_working_time;
  private LocalTime first_check_in;
  private LocalTime last_check_out;
  private List<CheckInCheckOutResponse> check_in_check_outs;
}