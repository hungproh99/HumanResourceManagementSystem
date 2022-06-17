package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckInCheckOutResponse {
	private Long checkin_checkout_id;
	private Long timekeeping_id;
	private LocalTime checkin;
	private LocalTime checkout;
}