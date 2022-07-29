package com.csproject.hrm.services;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;

import java.util.List;
import java.util.Optional;

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

  Optional<BankResponse> findBankByEmployeeID(String employeeID);

  Optional<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID);

  Optional<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID);

  Optional<EmployeeDetailResponse> findMainDetail(String employeeID);

  List<EmployeeNameAndID> getAllEmployeeByManagerID(String managerId);
}