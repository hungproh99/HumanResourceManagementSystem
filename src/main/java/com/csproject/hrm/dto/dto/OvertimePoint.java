package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OvertimePoint {
  private String type;
  private Double point;
}
