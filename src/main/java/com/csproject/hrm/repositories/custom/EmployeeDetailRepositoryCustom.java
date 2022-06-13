package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.RelativeInformation;
import com.csproject.hrm.entities.WorkingHistory;

import java.util.List;

public interface EmployeeDetailRepositoryCustom {
  List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID);
  void updateRelativeInfo(RelativeInformationRequest relativeInformation);
  void updateWorkingHistory(WorkingHistoryRequest workingHistory);
  void updateEducationInfo(EducationRequest education);
  void updateBankInfo(BankRequest bank);
  void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance);
  void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest);
  List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID);
  List<EducationResponse> findEducationByEmployeeID(String employeeID);
  List<BankResponse> findBankByEmployeeID(String employeeID);
  List<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID);
  List<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID);
  List<EmployeeDetailResponse> findMainDetail(String employeeID);
  public boolean checkEmployeeIDIsExists(String employeeID);
}