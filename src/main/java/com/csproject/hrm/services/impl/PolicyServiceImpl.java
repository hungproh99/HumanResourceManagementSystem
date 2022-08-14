package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.PolicyRepository;
import com.csproject.hrm.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

  @Autowired PolicyRepository policyRepository;

  @Override
  public ListPolicyResponse getListPolicyByCategoryID(QueryParam queryParam) {
    List<PolicyResponse> policyResponses = policyRepository.getListPolicyByCategoryID(queryParam);
    policyResponses.forEach(
        policyResponse -> {
          policyResponse.setPolicy_type(EPolicyType.getLabel(policyResponse.getPolicy_type()));
          policyResponse.setPolicy_category(
              EPolicyCategory.getLabel(policyResponse.getPolicy_category()));
          policyResponse.setPolicy_name(EPolicyName.getLabel(policyResponse.getPolicy_name()));
        });
    int total = policyRepository.getTotalPolicyByCategoryID(queryParam);
    return ListPolicyResponse.builder().policyResponseList(policyResponses).total(total).build();
  }

  @Override
  public List<PolicyCategoryResponse> getAllPolicyCategory() {
    List<PolicyCategoryResponse> policyCategoryResponseList =
        policyRepository.getAllPolicyCategory();
    policyCategoryResponseList.forEach(
        policyCategoryResponse ->
            policyCategoryResponse.setPolicy_category(
                EPolicyCategory.getLabel(policyCategoryResponse.getPolicy_category())));
    return policyCategoryResponseList;
  }
}