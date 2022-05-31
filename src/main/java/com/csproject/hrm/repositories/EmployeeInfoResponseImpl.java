package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeInfoResponseImpl implements EmployeeInfoResponse {
	//	private String employeeID;
	//	private String fullName;
	//	private String nickName;
	//	private String birthday;
	//	private String gender;
	//	private String address;
	//	private String avatar;
	//	private String facebook;
	//	private String workStatus;
	//	private String phone;
	//	private String job;
	//	private Bank bank;
	//	private List<Education> education;
	//	private String companyEmail;
	//	private String personalEmail;
	//	private String area;
	//	private String office;
	//	private Date startDate;
	//	private List<WorkingContract> contract;
	//	private List<RelativeInformation> relative;
	//	private List<WorkHistory> workHistory;
	//	private List<Insurance> insurance;
	//	private List<Tax> tax;
	//	private IdentityCard identity;
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<Employee> getCustomEmployee() {
		String sql = "select e from Employee e";
		final TypedQuery<Employee> query = entityManager.createQuery(sql, Employee.class);
		return query.getResultList();
	}
}