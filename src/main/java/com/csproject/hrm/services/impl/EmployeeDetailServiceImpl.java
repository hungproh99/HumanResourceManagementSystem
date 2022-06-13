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
		    || relativeInformation.getRelativeTypeId() == null
		    || relativeInformation.getEmployeeId() == null
		    || relativeInformation.getContact() == null
		    || relativeInformation.getParentName() == null
		    || relativeInformation.getStatus() == null
		    || relativeInformation.getBirthDate() == null) {
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
		    || workingHistory.getCompanyName() == null
		    || workingHistory.getEmployeeId() == null
		    || workingHistory.getPosition() == null
		    || workingHistory.getEndDate() == null
		    || workingHistory.getStartDate() == null) {
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
		    || education.getCertificate() == null
		    || education.getStatus() == null
		    || education.getEmployeeId() == null
		    || education.getNameSchool() == null
		    || education.getEndDate() == null
		    || education.getStartDate() == null) {
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
		if (bank.getId() == null
		    || bank.getNameBank() == null
		    || bank.getEmployeeId() == null
		    || bank.getAddress() == null
		    || bank.getAccountName() == null
		    || bank.getAccountNumber() == null) {
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
		if (taxAndInsurance.getTaxCode() == null
		    || taxAndInsurance.getInsurancePercent() == null
		    || taxAndInsurance.getEmployeeId() == null
		    || taxAndInsurance.getInsuranceAddress() == null
		    || taxAndInsurance.getInsuranceName() == null
		    || taxAndInsurance.getInsuranceId() == null
		    || taxAndInsurance.getInsuranceTitle() == null
		    || taxAndInsurance.getInsuranceDescription() == null) {
			throw new NullPointerException("Had param is null!");
		}
		String employeeID=taxAndInsurance.getEmployeeId();
		if(!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)){
			throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
		}
		employeeDetailRepository.updateTaxAndInsurance(taxAndInsurance);
	}
	
	@Override
	public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
		if (employeeDetailRequest.getEmployee_id() == null
		    || employeeDetailRequest.getPersonal_email() == null
		    || employeeDetailRequest.getCompany_email() == null
		    || employeeDetailRequest.getFull_name() == null
		    || employeeDetailRequest.getGender() == null
		    || employeeDetailRequest.getAddress() == null
		    || employeeDetailRequest.getPhone_number() == null
		    || employeeDetailRequest.getBirth_date() == null
		    || employeeDetailRequest.getMarital_status() == null
		    || employeeDetailRequest.getWorking_status() == null
		    || employeeDetailRequest.getRole_type() == null
		    || employeeDetailRequest.getManager_id() == null
		    || employeeDetailRequest.getAvatar() == null
		    || employeeDetailRequest.getNick_name() == null
		    || employeeDetailRequest.getFacebook() == null
		    || employeeDetailRequest.getTax_code() == null
		    || employeeDetailRequest.getCurrent_situation() == null
		    || employeeDetailRequest.getEmployee_type_id() == null
		    || employeeDetailRequest.getWorking_type_id() == null
		    || employeeDetailRequest.getWorking_contract_id() == null
		    || employeeDetailRequest.getCompany_name() == null
		    || employeeDetailRequest.getContract_type_id() == null
		    || employeeDetailRequest.getStart_date() == null
		    || employeeDetailRequest.getEnd_date() == null
		    || employeeDetailRequest.getBase_salary() == null
		    || employeeDetailRequest.getContract_url() == null
		    || employeeDetailRequest.getContract_status() == null
		    || employeeDetailRequest.getArea_id() == null
		    || employeeDetailRequest.getOffice_id() == null
		    || employeeDetailRequest.getGrade_id() == null) {
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