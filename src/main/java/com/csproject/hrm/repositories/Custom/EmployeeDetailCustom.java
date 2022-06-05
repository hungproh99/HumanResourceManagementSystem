package com.csproject.hrm.repositories.Custom;

import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.Bank;
import com.csproject.hrm.entities.Education;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface EmployeeDetailCustom {
	List<EmployeeDetailResponse> findMainDetail(QueryParam queryParam);
	
	List<TaxAndInsuranceResponse> findTaxAndInsurance(QueryParam queryParam);
	
	List<EmployeeAdditionalInfo> findAdditionalInfo(QueryParam queryParam);
	
	List<Bank> findBankByEmployeeID(QueryParam queryParam);
	
	List<Education> findEducationByEmployeeID(QueryParam queryParam);
}