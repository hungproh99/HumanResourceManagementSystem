package com.csproject.hrm.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationsRequestRequestC {
  private Long applicationRequestId;
  private String createEmployeeId;
  private Long requestNameId;
  private Long requestStatusId;
  private Long requestTypeId;
  private String description;
  private LocalDate createDate;
  private LocalDate latestDate;
  private LocalDate duration;
  private String approver;
  private String data;
  private Boolean isBookmark;
  private Boolean isRemind;
  private Boolean isRead;
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
}