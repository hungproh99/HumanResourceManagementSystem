package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.WorkingPlaceDto;
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

  boolean checkBankIsExisted(String employeeId);

  void updateBankInfo(BankRequest bank);

  void updateInsurance(TaxAndInsuranceRequest taxAndInsurance);

  void updateTax(TaxAndInsuranceRequest taxAndInsurance);

  void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo);

  void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest);

  WorkingPlaceDto getWorkingPlaceByContractID(Boolean status, Long contractID);

  List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID);

  CareerHistoryResponse findCareerHistoryByEmployeeID(String employeeID);

  List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID);

  List<EducationResponse> findEducationByEmployeeID(String employeeID);

  BankResponse findBankByEmployeeID(String employeeID);

  EmployeeAdditionalInfo findAdditionalInfo(String employeeID);

  EmployeeDetailResponse findMainDetail(String employeeID);

  WorkingInfoResponse findWorkingInfo(String employeeID);

  WorkingInfoResponse findNewWorkingInfo(String employeeID);

  boolean checkEmployeeIDIsExists(String employeeID);

  String getManagerByEmployeeID(String employeeID);

  String getEmployeeInfoByEmployeeID(String employeeID);

  Integer getLevelByEmployeeID(String employeeID);

  int countNumberDependentRelative(String employeeId);

  List<EmployeeNameAndID> getAllEmployeeByManagerID(String employeeID);

  void updateAvatar(AvatarRequest avatarRequest);

  void updateWorkingInfo(WorkingInfoRequest workingInfoRequest);

  RoleResponse getRole(String employeeID);

  void updateRole(RoleRequest roleRequest);
}