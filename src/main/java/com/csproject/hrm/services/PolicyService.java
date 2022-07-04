package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jooq.QueryParam;

public interface PolicyService {
  ListCAndBPolicyResponse getListCAndBPolicy(QueryParam queryParam);

  ListInsurancePolicyResponse getListInsurancePolicy(QueryParam queryParam);

  ListWorkingPolicyResponse getListWorkingPolicy(QueryParam queryParam);

  ListLeavePolicyResponse getListLeavePolicy(QueryParam queryParam);

  ListTaxPolicyResponse getListTaxPolicy(QueryParam queryParam);
}