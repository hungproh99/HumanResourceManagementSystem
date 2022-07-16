package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeDetailRepositoryCustom {
  void updateRelativeInfo(RelativeInformationRequest relativeInformation);

  void updateWorkingHistory(WorkingHistoryRequest workingHistory);

  void updateEducationInfo(EducationRequest education);

  void updateBankInfo(BankRequest bank);

  void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance);

  void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo);

  void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest);

  List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID);

  Optional<CareerHistoryResponse> findCareerHistoryByEmployeeID(String employeeID);

  List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID);

  List<EducationResponse> findEducationByEmployeeID(String employeeID);

  Optional<BankResponse> findBankByEmployeeID(String employeeID);

  Optional<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID);

  Optional<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID);

  Optional<EmployeeDetailResponse> findMainDetail(String employeeID);

  boolean checkEmployeeIDIsExists(String employeeID);

  String getManagerByEmployeeID(String employeeID);

  String getEmployeeInfoByEmployeeID(String employeeID);

  Integer getLevelByEmployeeID(String employeeID);
}