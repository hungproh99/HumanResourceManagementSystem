package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInCheckOutDto {
  private Long checkin_checkout_id;
  private LocalTime checkin;
  private LocalTime checkout;
}
