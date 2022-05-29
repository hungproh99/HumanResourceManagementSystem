package com.csproject.hrm.dto.respone;

import com.csproject.hrm.entities.*;

import java.sql.Date;
import java.util.List;

public class EmployeeInfoResponse {
	private String employeeID;
	private String fullName;
	private String nickName;
	private String birthday;
	private String gender;
	private String address;
	private String avatar;
	private String facebook;
	private Bank bank;
	private List<Education> education;
	private String companyEmail;
	private String personalEmail;
	private String workStatus;
	private String job;
	private String area;
	private String office;
	private Date startDate;
	private List<WorkingContract> contract;
	private List<RelativeInformation> relative;
	private List<WorkHistory> workHistory;
	private String phone;
	private List<Insurance> insurance;
	private List<Tax> tax;
	private IdentityCard identity;
}