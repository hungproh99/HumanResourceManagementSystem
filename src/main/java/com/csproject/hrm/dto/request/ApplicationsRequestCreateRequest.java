package com.csproject.hrm.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationsRequestCreateRequest {
  private Long applicationRequestId;

  @NotBlank(message = "createEmployeeId must not be blank!")
  private String createEmployeeId;

  @Positive(message = "requestNameId must be a positive number!")
  private Long requestNameId;

  private Long requestStatusId;

  @Positive(message = "requestTypeId must be a positive number!")
  private Long requestTypeId;

  @NotBlank(message = "description must not be blank!")
  private String description;

  private LocalDateTime createDate;
  private LocalDateTime latestDate;
  private LocalDateTime duration;
  private String approver;
  private String data;
  private Boolean isBookmark;
  private Boolean isRemind;
  private String employeeId;
  private String employeeName;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private LocalDate date;
  private List<String> taxType;
  private String currentTitle;
  private String desiredTitle;
  private String currentArea;
  private String desiredArea;
  private String currentOffice;
  private String desiredOffice;
  private String value;
  private String type;
  private String reason;
}