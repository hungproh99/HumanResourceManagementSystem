package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDetailRepositoryCustom {
  TaxAndInsuranceResponse findTaxAndInsurance(String employeeID);

  void updateRelativeInfo(RelativeInformationRequest relativeInformation);

  void updateWorkingHistory(WorkingHistoryRequest workingHistory);

  void updateEducationInfo(EducationRequest education);

  void updateBankInfo(BankRequest bank);

  void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance);

  void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo);

  void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest);

  List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID);

  CareerHistoryResponse findCareerHistoryByEmployeeID(String employeeID);

  List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID);

  List<EducationResponse> findEducationByEmployeeID(String employeeID);

  BankResponse findBankByEmployeeID(String employeeID);

  EmployeeAdditionalInfo findAdditionalInfo(String employeeID);

  EmployeeDetailResponse findMainDetail(String employeeID);

  WorkingInfoResponse findWorkingInfo(String employeeID);

  boolean checkEmployeeIDIsExists(String employeeID);

  String getManagerByEmployeeID(String employeeID);

  String getEmployeeInfoByEmployeeID(String employeeID);

  Integer getLevelByEmployeeID(String employeeID);

  int countNumberDependentRelative(String employeeId);

  List<EmployeeNameAndID> getAllEmployeeByManagerID(String employeeID);
}