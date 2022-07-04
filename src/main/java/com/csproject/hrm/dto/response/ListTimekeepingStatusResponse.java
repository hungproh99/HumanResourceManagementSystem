package com.csproject.hrm.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListTimekeepingStatusResponse {
  private Long list_id;
  private Long timekeeping_id;
  private String timekeeping_status_name;
}