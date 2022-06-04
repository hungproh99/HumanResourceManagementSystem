package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponse {
	private String full_name;
	private String employee_id;
	private LocalDate email;
	private LocalDate seniority;
	private LocalDate officialDate;
	private String work_status;
	private String contract_url;
	private String phone;
	private String job;
	private LocalDate birth_date;
	private String companyEmail;
	private String gender;
	private String maritalStatus;
	private String office;
	private String contract;
	private String area;
	private String avatar;
	
}