package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaDto {
  private long area_id;
  private String name;
  private String manager_id;
}