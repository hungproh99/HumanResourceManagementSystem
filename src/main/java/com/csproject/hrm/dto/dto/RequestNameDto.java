package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestNameDto {
  private Long request_name_id;
  private String request_name_name;
}