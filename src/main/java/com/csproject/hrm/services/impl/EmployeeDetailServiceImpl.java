package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.*;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.services.EmployeeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class EmployeeDetailServiceImpl implements EmployeeDetailService {

  @Autowired EmployeeDetailRepository employeeDetailRepository;

  @Override
  public List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      return employeeDetailRepository.findRelativeByEmployeeID(employeeID);
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }

  @Override
  public void updateRelativeInfo(RelativeInformationRequest relativeInformation) {
    String employeeID = relativeInformation.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (!relativeInformation.getContact().matches(PHONE_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_PHONE_FORMAT);
    }
    if (relativeInformation.getParentName() == null
        || relativeInformation.getRelativeTypeId() == null
        || relativeInformation.getStatus() == null
        || relativeInformation.getBirthDate() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateRelativeInfo(relativeInformation);
  }

  @Override
  public void updateWorkingHistory(WorkingHistoryRequest workingHistory) {
    String employeeID = workingHistory.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (workingHistory.getCompanyName() == null
        || workingHistory.getPosition() == null
        || workingHistory.getStartDate() == null
        || workingHistory.getEndDate() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateWorkingHistory(workingHistory);
  }

  @Override
  public void updateEducationInfo(EducationRequest education) {
    String employeeID = education.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (education.getNameSchool() == null
        || education.getCertificate() == null
        || education.getStartDate() == null
        || education.getEndDate() == null
        || education.getStatus() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateEducationInfo(education);
  }

  @Override
  public void updateBankInfo(BankRequest bank) {
    String employeeID = bank.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (bank.getNameBank() == null
        || bank.getAddress() == null
        || bank.getAccountNumber() == null
        || bank.getAccountName() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateBankInfo(bank);
  }

  @Override
  public void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance) {
    String employeeID = taxAndInsurance.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    TaxAndInsuranceResponse response = employeeDetailRepository.findTaxAndInsurance(employeeID);
    if (taxAndInsurance.getTaxCode() == null || "".equals(taxAndInsurance.getTaxCode())) {
      if (taxAndInsurance.getInsuranceId() == null
          || taxAndInsurance.getInsuranceAddress() == null
          || taxAndInsurance.getPolicyNameId() == null) {
        throw new CustomParameterConstraintException(FILL_NOT_FULL);
      } else {
        employeeDetailRepository.updateInsurance(taxAndInsurance);
      }
    } else {
      employeeDetailRepository.updateTax(taxAndInsurance);
    }
  }

  @Override
  public void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo) {
    String employeeID = employeeAdditionalInfo.getEmployee_id();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (!employeeAdditionalInfo.getPhone_number().matches(PHONE_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_PHONE_FORMAT);
    }
    if (!employeeAdditionalInfo.getPersonal_email().matches(EMAIL_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_EMAIL_FORMAT);
    }
    if (employeeAdditionalInfo.getAddress() == null
        || employeeAdditionalInfo.getPlace_of_origin() == null
        || employeeAdditionalInfo.getPlace_of_residence() == null
        || employeeAdditionalInfo.getNationality() == null
        || employeeAdditionalInfo.getCard_id() == null
        || employeeAdditionalInfo.getProvidePlace() == null
        || employeeAdditionalInfo.getProvideDate() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateAdditionalInfo(employeeAdditionalInfo);
  }

  @Override
  public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
    String employeeID = employeeDetailRequest.getEmployee_id();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (!employeeDetailRequest.getPhone_number().matches(PHONE_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_PHONE_FORMAT);
    }
    if (!employeeDetailRequest.getCompany_email().matches(EMAIL_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_EMAIL_FORMAT);
    }
    if (employeeDetailRequest.getFull_name() == null
        || employeeDetailRequest.getGender() == null
        || employeeDetailRequest.getBirth_date() == null
        || employeeDetailRequest.getWorking_status() == null
        || employeeDetailRequest.getWorking_contract_id() == null
        || employeeDetailRequest.getStart_date() == null
        || employeeDetailRequest.getEnd_date() == null
        || employeeDetailRequest.getArea_id() == null
        || employeeDetailRequest.getOffice_id() == null
        || employeeDetailRequest.getJob_id() == null
        || employeeDetailRequest.getGrade_id() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    long yearToCurrent =
        ChronoUnit.YEARS.between(
            LocalDate.now(ZoneId.of("UTC+07")), employeeDetailRequest.getBirth_date());
    if (Math.abs(yearToCurrent) < 18L) {
      throw new CustomErrorException(
          "Birthdate must be before " + LocalDate.now(ZoneId.of("UTC+07")).minusYears(18L));
    }
    employeeDetailRepository.updateEmployeeDetail(employeeDetailRequest);
  }

  @Override
  public List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      return employeeDetailRepository.findWorkingHistoryByEmployeeID(employeeID);
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }

  @Override
  public List<EducationResponse> findEducationByEmployeeID(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      return employeeDetailRepository.findEducationByEmployeeID(employeeID);
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }

  @Override
  public BankResponse findBankByEmployeeID(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      return employeeDetailRepository.findBankByEmployeeID(employeeID);
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }

  @Override
  public EmployeeAdditionalInfo findAdditionalInfo(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      return employeeDetailRepository.findAdditionalInfo(employeeID);
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }

  @Override
  public TaxAndInsuranceResponse findTaxAndInsurance(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      return employeeDetailRepository.findTaxAndInsurance(employeeID);
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }

  @Override
  public EmployeeDetailResponse findMainDetail(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    EmployeeDetailResponse employeeDetailResponse =
        employeeDetailRepository.findMainDetail(employeeID);
    employeeDetailResponse.setArea_name(EArea.getLabel(employeeDetailResponse.getArea_name()));
    employeeDetailResponse.setOffice_name(
        EOffice.getLabel(employeeDetailResponse.getOffice_name()));
    employeeDetailResponse.setPosition_name(
        EJob.getLabel(employeeDetailResponse.getPosition_name()));
    employeeDetailResponse.setGrade(EGradeType.getLabel(employeeDetailResponse.getGrade()));
    employeeDetailResponse.setWorking_name(
        EWorkingType.getLabel(employeeDetailResponse.getWorking_name()));
    return employeeDetailResponse;
  }

  @Override
  public WorkingInfoResponse findWorkingInfo(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    WorkingInfoResponse workingInfo = employeeDetailRepository.findWorkingInfo(employeeID);
    workingInfo.setArea(EArea.getLabel(workingInfo.getArea()));
    workingInfo.setOffice(EOffice.getLabel(workingInfo.getOffice()));
    workingInfo.setPosition(EJob.getLabel(workingInfo.getPosition()));
    workingInfo.setWorking_type(EWorkingType.getLabel(workingInfo.getWorking_type()));
    workingInfo.setGrade(EGradeType.getLabel(workingInfo.getGrade()));
    workingInfo.setEmployee_type(EEmployeeType.getLabel(workingInfo.getEmployee_type()));
    return workingInfo;
  }

  @Override
  public List<EmployeeNameAndID> getAllEmployeeByManagerID(String employeeID) {
    List<EmployeeNameAndID> list = employeeDetailRepository.getAllEmployeeByManagerID(employeeID);
    if (list.size() <= 0) {
      return list;
    }
    List<EmployeeNameAndID> list2 = new ArrayList<>();
    for (EmployeeNameAndID employee : list) {
      list2.addAll(getAllEmployeeByManagerID(employee.getEmployeeID()));
    }
    if (list2.size() > 0) {
      list.addAll(list2);
    }
    return list;
  }

  @Override
  public void updateAvatar(AvatarRequest avatarRequest) {
    String employeeID = avatarRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (avatarRequest.getAvatar() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateAvatar(avatarRequest);
  }

  @Override
  public void updateWorkingInfo(WorkingInfoRequest workingInfoRequest) {
    String employeeID = workingInfoRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (workingInfoRequest.getBaseSalary() == null
        || workingInfoRequest.getFinalSalary() == null
        || workingInfoRequest.getOffice() == null
        || workingInfoRequest.getPosition() == null
        || workingInfoRequest.getWorkingTypeId() == null
        || workingInfoRequest.getStartDate() == null
        || workingInfoRequest.getArea() == null
        || workingInfoRequest.getEmployeeType() == null
        || workingInfoRequest.getManagerId() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateWorkingInfo(workingInfoRequest);
  }

  @Override
  public RoleResponse getRole(String employeeID) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    RoleResponse roleResponse = employeeDetailRepository.getRole(employeeID);
    roleResponse.setRoleName(ERole.getLabel(roleResponse.getRoleName()));
    return roleResponse;
  }

  @Override
  public void updateRole(RoleRequest roleRequest) {
    String employeeID = roleRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    if (roleRequest.getRoleId() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    employeeDetailRepository.updateRole(roleRequest);
  }
}