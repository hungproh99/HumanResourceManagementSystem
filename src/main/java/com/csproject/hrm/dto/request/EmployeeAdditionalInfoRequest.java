package com.csproject.hrm.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAdditionalInfoRequest {
	private String address;
	private String place_of_residence;
	private String place_of_origin;
	private String nationality;
	private String card_id;
	private LocalDate provideDate;
	private String providePlace;
	private String personal_email;
	private String phone_number;
	private String nick_name;
	private String facebook;
	private String employee_id;
}