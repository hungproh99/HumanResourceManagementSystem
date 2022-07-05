package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EPolicyCategory;
import com.csproject.hrm.common.enums.EPolicyType;
import com.csproject.hrm.dto.response.ListPolicyResponse;
import com.csproject.hrm.dto.response.PolicyResponse;
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
    List<PolicyResponse> policyRespons = policyRepository.getListPolicyByCategoryID(queryParam);
    policyRespons.forEach(
        policyResponse -> {
          policyResponse.setPolicy_type(EPolicyType.getLabel(policyResponse.getPolicy_type()));
          policyResponse.setPolicy_category(
              EPolicyCategory.getLabel(policyResponse.getPolicy_category()));
        });
    int total = policyRepository.getTotalPolicyByCategoryID(queryParam);
    return ListPolicyResponse.builder().policyResponseList(policyRespons).total(total).build();
  }
}