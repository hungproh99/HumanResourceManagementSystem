package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.TaxPolicyResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface TaxPolicyRepositoryCustom {
  List<TaxPolicyResponse> getListTaxPolicy(QueryParam queryParam);

  int getTotalTaxPolicy(QueryParam queryParam);
}