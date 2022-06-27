package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusDto {
  private Long request_status_id;
  private String request_status_name;
}