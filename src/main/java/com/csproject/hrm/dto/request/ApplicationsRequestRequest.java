package com.csproject.hrm.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationsRequestRequest {
  private String employee_id;
  private Long request_type_id;
  private Long request_name_id;
  private String full_name;
  private LocalDateTime created_date;
  private LocalDateTime latest_date;
  private LocalDateTime duration;
  private String description;
  private LocalDateTime change_status_time;
  private String approver;
}