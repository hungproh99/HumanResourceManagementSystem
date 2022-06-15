package com.csproject.hrm.services;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;

import java.util.List;

public interface EmployeeDetailService {
	
	void updateRelativeInfo(RelativeInformationRequest relativeInformation);
	
	void updateWorkingHistory(WorkingHistoryRequest workingHistory);
	
	void updateEducationInfo(EducationRequest education);
	
	void updateBankInfo(BankRequest bank);
	
	void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance);
	
	void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo);
	
	void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest);
	
	List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID);
	
	List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID);
	
	List<EducationResponse> findEducationByEmployeeID(String employeeID);
	
	List<BankResponse> findBankByEmployeeID(String employeeID);
	
	List<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID);
	
	List<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID);
	
	List<EmployeeDetailResponse> findMainDetail(String employeeID);
}