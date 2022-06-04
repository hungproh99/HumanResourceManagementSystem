package com.csproject.hrm.repositories.Impl;

import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepositoryCustom {
	List<HrmResponse> findAllEmployee(QueryParam queryParam);
	
}