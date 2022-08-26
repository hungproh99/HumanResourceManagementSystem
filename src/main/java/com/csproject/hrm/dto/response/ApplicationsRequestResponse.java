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
public class ApplicationsRequestResponse {
  private Long application_request_id;
  private String employee_id;
  private String full_name;
  private LocalDateTime create_date;
  private String request_type;
  private String request_name;
  private String description;
  private String request_status_name;
  private LocalDateTime change_status_time;
  private LocalDateTime duration;
  private String approver;
  private List<String> checked_by;
  private String is_bookmark;
  private String is_enough_level;
  private String type;
  private String comment;
}
