package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimekeepingResponse {
  private String full_name;
  private String position;
  private String grade;
  private Date current_date;
  private String timekeeping_status;
  private Time first_check_in;
  private Time last_check_out;
}
