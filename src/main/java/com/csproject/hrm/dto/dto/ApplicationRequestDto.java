package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestDto {
  private Long requestId;
  private String employeeId;
  private String requestName;
  private String requestType;
  private String comment;
  private LocalDateTime latestDate;
  private String approveId;
  private String data;
}
