package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.*;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.services.EmployeeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class EmployeeDetailServiceImpl implements EmployeeDetailService {
	
	@Autowired
	EmployeeDetailRepository employeeDetailRepository;
	
	@Override
	public List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID) {
		if (employeeID == null) {
			throw new NullPointerException("Param employeeID is null!");
		}
		if(employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			return employeeDetailRepository.findRelativeByEmployeeID(employeeID);
		}else{
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
	}
	
	@Override
	public void updateRelativeInfo(RelativeInformationRequest relativeInformation) {
		if (relativeInformation.getId() == null
		    || relativeInformation.getEmployeeId() == null) {
			throw new NullPointerException("Had param is null!");
		}
		String employeeID=relativeInformation.getEmployeeId();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		if (!relativeInformation.getContact().matches(PHONE_VALIDATION)) {
			throw new CustomParameterConstraintException(INVALID_PHONE_FORMAT);
		}
		employeeDetailRepository.updateRelativeInfo(relativeInformation);
	}
	
	@Override
	public void updateWorkingHistory(WorkingHistoryRequest workingHistory) {
		if (workingHistory.getId() == null
		    || workingHistory.getEmployeeId() == null) {
			throw new NullPointerException("Had param is null!");
		}
		String employeeID=workingHistory.getEmployeeId();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		employeeDetailRepository.updateWorkingHistory(workingHistory);
	}
	
	@Override
	public void updateEducationInfo(EducationRequest education) {
		if (education.getId() == null
		    || education.getEmployeeId() == null) {
			throw new NullPointerException("Had param is null!");
		}
		String employeeID=education.getEmployeeId();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		employeeDetailRepository.updateEducationInfo(education);
	}
	
	@Override
	public void updateBankInfo(BankRequest bank) {
		if (bank.getId() == null || bank.getEmployeeId() == null) {
			throw new NullPointerException("Had param is null!");
		}
		String employeeID=bank.getEmployeeId();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		employeeDetailRepository.updateBankInfo(bank);
	}
	
	@Override
	public void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance) {
		if (taxAndInsurance.getEmployeeId() == null) {
			throw new NullPointerException("Had param is null!");
		}
		String employeeID=taxAndInsurance.getEmployeeId();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		employeeDetailRepository.updateTaxAndInsurance(taxAndInsurance);
	}
	
	@Override
	public void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo) {
		
		String employeeID=employeeAdditionalInfo.getEmployee_id();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		employeeDetailRepository.updateAdditionalInfo(employeeAdditionalInfo);
	}
	
	@Override
	public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
		if (employeeDetailRequest.getEmployee_id() == null) {
			throw new NullPointerException("Had param is null!");
		}
		String employeeID=employeeDetailRequest.getEmployee_id();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		employeeDetailRepository.updateEmployeeDetail(employeeDetailRequest);
	}
	
	@Override
	public List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID) {
		if (employeeID == null) {
			throw new NullPointerException("Param employeeID is null!");
		}
		if(employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			return employeeDetailRepository.findWorkingHistoryByEmployeeID(employeeID);
		}else{
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
	}
	
	@Override
	public List<EducationResponse> findEducationByEmployeeID(String employeeID) {
		if (employeeID == null) {
			throw new NullPointerException("Param employeeID is null!");
		}
		if(employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			return  employeeDetailRepository.findEducationByEmployeeID(employeeID);
		}else{
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
	}
	
	@Override
	public List<BankResponse> findBankByEmployeeID(String employeeID) {
		if (employeeID == null) {
			throw new NullPointerException("Param employeeID is null!");
		}
		if(employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			return employeeDetailRepository.findBankByEmployeeID(employeeID);
		}else{
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
	}
	
	@Override
	public List<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID) {
		if (employeeID == null) {
			throw new NullPointerException("Param employeeID is null!");
		}
		if(employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			return employeeDetailRepository.findAdditionalInfo(employeeID);
		}else{
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
	}
	
	@Override
	public List<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID) {
		if (employeeID == null) {
			throw new NullPointerException("Param employeeID is null!");
		}
		if(employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			return  employeeDetailRepository.findTaxAndInsurance(employeeID);
		}else{
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
	}
	
	@Override
	public List<EmployeeDetailResponse> findMainDetail(String employeeID) {
		if (employeeID == null) {
			throw new NullPointerException("Param employeeID is null!");
		}
		if(employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			return employeeDetailRepository.findMainDetail(employeeID);
		}else{
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
	}
}