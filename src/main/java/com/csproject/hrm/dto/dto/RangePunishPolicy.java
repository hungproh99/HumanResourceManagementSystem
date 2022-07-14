package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RangePunishPolicy {
  private Long minTime;
  private Long maxTime;
  private BigDecimal minus;
}
