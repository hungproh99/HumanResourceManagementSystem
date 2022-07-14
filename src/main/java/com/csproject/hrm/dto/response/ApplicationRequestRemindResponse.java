package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationRequestRemindResponse {
  private Long application_request_id;
  private String request_type;
  private String request_name;
  private LocalDateTime create_date;
  private String full_name;
  private String employee_id;
  private String approver;
  private List<String> checked_by;
}
