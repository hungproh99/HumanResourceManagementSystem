package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimekeepingResponseList {
  private String employee_id;
  private String full_name;
  private String position;
  private String grade;
  private List<TimekeepingResponse> timekeepingResponses;
}
