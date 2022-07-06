package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.ListPolicyResponse;
import com.csproject.hrm.jooq.QueryParam;

public interface PolicyService {
  ListPolicyResponse getListPolicyByCategoryID(QueryParam queryParam);
}