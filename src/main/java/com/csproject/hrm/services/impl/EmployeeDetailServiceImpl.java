package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.services.EmployeeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeDetailServiceImpl implements EmployeeDetailService {
	
	@Autowired
	EmployeeDetailRepository employeeDetailRepository;
	
	@Override
	public List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID) {
		return employeeDetailRepository.findRelativeByEmployeeID(employeeID);
	}
	
	@Override
	public void updateRelativeInfo(RelativeInformationRequest relativeInformation) {
		employeeDetailRepository.updateRelativeInfo(relativeInformation);
	}
	
	@Override
	public void updateWorkingHistory(WorkingHistoryRequest workingHistory) {
		employeeDetailRepository.updateWorkingHistory(workingHistory);
	}
	
	@Override
	public void updateEducationInfo(EducationRequest education) {
		employeeDetailRepository.updateEducationInfo(education);
	}
	
	@Override
	public void updateBankInfo(BankRequest bank) {
		employeeDetailRepository.updateBankInfo(bank);
	}
	
	@Override
	public void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance) {
		employeeDetailRepository.updateTaxAndInsurance(taxAndInsurance);
	}
	
	@Override
	public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
		employeeDetailRepository.updateEmployeeDetail(employeeDetailRequest);
	}
	
	@Override
	public List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID) {
		return employeeDetailRepository.findWorkingHistoryByEmployeeID(employeeID);
	}
	
	@Override
	public List<EducationResponse> findEducationByEmployeeID(String employeeID) {
		return  employeeDetailRepository.findEducationByEmployeeID(employeeID);
	}
	
	@Override
	public List<BankResponse> findBankByEmployeeID(String employeeID) {
		return employeeDetailRepository.findBankByEmployeeID(employeeID);
	}
	
	@Override
	public List<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID) {
		return employeeDetailRepository.findAdditionalInfo(employeeID);
	}
	
	@Override
	public List<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID) {
		return  employeeDetailRepository.findTaxAndInsurance(employeeID);
	}
	
	@Override
	public List<EmployeeDetailResponse> findMainDetail(String employeeID) {
		return employeeDetailRepository.findMainDetail(employeeID);
	}
}