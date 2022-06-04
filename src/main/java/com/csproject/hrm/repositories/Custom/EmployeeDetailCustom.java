package com.csproject.hrm.repositories.Custom;

import com.csproject.hrm.dto.response.EmployeeDetailResponse;
import com.csproject.hrm.dto.response.TaxAndInsuranceResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface EmployeeDetailCustom {
	List<EmployeeDetailResponse> findMainDetail(QueryParam queryParam);
	
	List<TaxAndInsuranceResponse> findTaxAndInsurance(QueryParam queryParam);
}