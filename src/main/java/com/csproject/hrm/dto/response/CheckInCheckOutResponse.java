package com.csproject.hrm.dto.response;

import com.csproject.hrm.entities.Timekeeping;
import lombok.*;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckInCheckOutResponse {
	private Long checkin_checkout_id;
	private Timekeeping timekeeping_id;
	private LocalTime checkin;
	private LocalTime checkout;
}