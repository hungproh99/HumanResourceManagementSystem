package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.ListPolicyResponse;
import com.csproject.hrm.dto.response.PolicyCategoryResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface PolicyService {
  ListPolicyResponse getListPolicyByCategoryID(QueryParam queryParam);

  List<PolicyCategoryResponse> getAllPolicyCategory();
}