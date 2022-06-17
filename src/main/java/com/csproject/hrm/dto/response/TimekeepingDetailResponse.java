package com.csproject.hrm.dto.response;

import com.csproject.hrm.entities.CheckInCheckOut;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimekeepingDetailResponse {
  private String employee_id;
  private String full_name;
  private String manager_id;
  private String position;
  private String grade;
  private Date current_date;
  private Long timekeeping_id;
  private String timekeeping_status;
  private String total_working_time;
  private LocalTime first_check_in;
  private LocalTime last_check_out;
  private List<CheckInCheckOutResponse> check_in_check_outs;
}