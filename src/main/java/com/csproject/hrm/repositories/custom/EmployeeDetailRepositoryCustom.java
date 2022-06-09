package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.*;

import java.util.List;

public interface EmployeeDetailRepositoryCustom {

  List<RelativeInformation> findRelativeByEmployeeID(String employeeID);

  void updateEmployee(Employee employee);

  List<WorkingHistory> findWorkingHistoryByEmployeeID(String employeeID);

  List<Education> findEducationByEmployeeID(String employeeID);

  List<Bank> findBankByEmployeeID(String employeeID);

  List<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID);

  List<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID);

  List<EmployeeDetailResponse> findMainDetail(String employeeID);
}