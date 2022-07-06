package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeDto {
  private Long request_type_id;
  private String request_type_name;
}