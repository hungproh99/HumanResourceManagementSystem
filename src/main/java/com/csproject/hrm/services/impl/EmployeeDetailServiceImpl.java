package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.services.EmployeeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    employeeDetailRepository.updateRelativeInfo(relativeInformation);
  }

  @Override
  public void updateWorkingHistory(WorkingHistoryRequest workingHistory) {
    String employeeID = workingHistory.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    employeeDetailRepository.updateWorkingHistory(workingHistory);
  }

  @Override
  public void updateEducationInfo(EducationRequest education) {
    String employeeID = education.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    employeeDetailRepository.updateEducationInfo(education);
  }

  @Override
  public void updateBankInfo(BankRequest bank) {
    String employeeID = bank.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    employeeDetailRepository.updateBankInfo(bank);
  }

  @Override
  public void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance) {
    String employeeID = taxAndInsurance.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    employeeDetailRepository.updateTaxAndInsurance(taxAndInsurance);
  }

  @Override
  public void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo) {
    String employeeID = employeeAdditionalInfo.getEmployee_id();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    employeeDetailRepository.updateAdditionalInfo(employeeAdditionalInfo);
  }

  @Override
  public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
    String employeeID = employeeDetailRequest.getEmployee_id();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
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
  public Optional<BankResponse> findBankByEmployeeID(String employeeID) {
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
  public Optional<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID) {
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
  public Optional<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID) {
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
  public Optional<EmployeeDetailResponse> findMainDetail(String employeeID) {
    if (employeeID == null) {
      throw new NullPointerException("Param employeeID is null!");
    }
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
    Optional<EmployeeDetailResponse> employeeDetailResponse =
        employeeDetailRepository.findMainDetail(employeeID);
    employeeDetailResponse
        .get()
        .setArea_name(EArea.getLabel(employeeDetailResponse.get().getArea_name()));
    employeeDetailResponse
        .get()
        .setOffice_name(EOffice.getLabel(employeeDetailResponse.get().getOffice_name()));
    employeeDetailResponse
        .get()
        .setPosition_name(EJob.getLabel(employeeDetailResponse.get().getPosition_name()));
    employeeDetailResponse
        .get()
        .setGrade(EGradeType.getLabel(employeeDetailResponse.get().getGrade()));
    employeeDetailResponse
        .get()
        .setWorking_name(EWorkingType.getLabel(employeeDetailResponse.get().getWorking_name()));
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
    return workingInfo;
  }

  @Override
  public List<EmployeeNameAndID> getAllEmployeeByManagerID(String managerId) {
    List<EmployeeNameAndID> list = employeeDetailRepository.getAllEmployeeByManagerID(managerId);
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
}