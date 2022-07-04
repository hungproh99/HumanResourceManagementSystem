package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.InsurancePolicyResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface InsurancePolicyRepositoryCustom {
  List<InsurancePolicyResponse> getListInsurancePolicy(QueryParam queryParam);

  int getTotalInsurancePolicy(QueryParam queryParam);
}