package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.PolicyDto;
import com.csproject.hrm.dto.response.PolicyCategoryResponse;
import com.csproject.hrm.dto.response.PolicyResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;
import java.util.Optional;

public interface PolicyRepositoryCustom {
  List<PolicyResponse> getListPolicyByCategoryID(QueryParam queryParam);

  int getTotalPolicyByCategoryID(QueryParam queryParam);

  List<PolicyCategoryResponse> getAllPolicyCategory();

  Long getTaxPolicyTypeIDByTaxName(String taxName);

  Optional<PolicyDto> getPolicyDtoByPolicyType(String policyType);
}