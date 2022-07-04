package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.CAndBPolicyResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface CAndBPolicyRepositoryCustom {
  List<CAndBPolicyResponse> getListCAndBPolicy(QueryParam queryParam);

  int getTotalCAndBPolicy(QueryParam queryParam);
}