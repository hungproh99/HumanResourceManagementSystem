package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.request.EmployeeDetailRequest;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.*;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface EmployeeDetailRepositoryCustom {
  List<EmployeeDetailResponse> findMainDetail(QueryParam queryParam);

  List<TaxAndInsuranceResponse> findTaxAndInsurance(QueryParam queryParam);

  List<EmployeeAdditionalInfo> findAdditionalInfo(QueryParam queryParam);

  List<Bank> findBankByEmployeeID(QueryParam queryParam);

  List<Education> findEducationByEmployeeID(QueryParam queryParam);

  List<WorkingHistory> findWorkingHistoryByEmployeeID(QueryParam queryParam);

  List<RelativeInformation> findRelativeByEmployeeID(QueryParam queryParam);

  void updateMainDetail(EmployeeDetailRequest detailRequest);
}