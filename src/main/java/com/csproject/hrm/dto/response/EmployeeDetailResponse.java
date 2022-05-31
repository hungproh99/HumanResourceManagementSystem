package com.csproject.hrm.dto.response;

import com.csproject.hrm.entities.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDetailResponse {
	private String employeeID;
	private String fullName;
	private String nickName;
	private String birthday;
	private String gender;
	private String address;
	private String avatar;
	private String facebook;
	private String workStatus;
	private String phone;
	private String job;
	private Bank bank;
	private List<Education> education;
	private String companyEmail;
	private String personalEmail;
	private String area;
	private String office;
	private List<WorkingContract> contract;
	private List<RelativeInformation> relative;
	private List<WorkHistory> workHistory;
	private IdentityCard identity;
}