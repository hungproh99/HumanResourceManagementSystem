package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimekeepingResponse {
  private long timekeeping_id;
  private Date current_date;
  private List<ListTimekeepingStatusResponse> timekeeping_status;
  private Time first_check_in;
  private Time last_check_out;
}
