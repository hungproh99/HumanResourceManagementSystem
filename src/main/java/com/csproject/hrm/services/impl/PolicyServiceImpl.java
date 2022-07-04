package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

  @Autowired WorkingPolicyRepository workingPolicyRepository;
  @Autowired CAndBPolicyRepository cAndBPolicyRepository;
  @Autowired InsurancePolicyRepository insurancePolicyRepository;
  @Autowired LeavePolicyRepository leavePolicyRepository;
  @Autowired TaxPolicyRepository taxPolicyRepository;

  @Override
  public ListWorkingPolicyResponse getListWorkingPolicy(QueryParam queryParam) {
    List<WorkingPolicyResponse> workingPolicyResponses =
        workingPolicyRepository.getListWorkingPolicy(queryParam);
    workingPolicyResponses.forEach(
        workingPolicyResponse -> {
          workingPolicyResponse.setWorking_policy_type(
              EWorkingPolicyType.getLabel(workingPolicyResponse.getWorking_policy_type()));
        });
    int total = workingPolicyRepository.getTotalWorkingPolicy(queryParam);
    return ListWorkingPolicyResponse.builder()
        .workingPolicyResponseList(workingPolicyResponses)
        .total(total)
        .build();
  }

  @Override
  public ListCAndBPolicyResponse getListCAndBPolicy(QueryParam queryParam) {
    List<CAndBPolicyResponse> cAndBPolicyResponses =
        cAndBPolicyRepository.getListCAndBPolicy(queryParam);
    cAndBPolicyResponses.forEach(
        workingPolicyResponse -> {
          workingPolicyResponse.setCompensation_benefit_policy_type(
              ECompensationBenefitPolicyType.getLabel(
                  workingPolicyResponse.getCompensation_benefit_policy_type()));
        });
    int total = cAndBPolicyRepository.getTotalCAndBPolicy(queryParam);
    return ListCAndBPolicyResponse.builder()
        .cAndBPolicyResponseList(cAndBPolicyResponses)
        .total(total)
        .build();
  }

  @Override
  public ListInsurancePolicyResponse getListInsurancePolicy(QueryParam queryParam) {
    List<InsurancePolicyResponse> workingPolicyResponses =
        insurancePolicyRepository.getListInsurancePolicy(queryParam);
    workingPolicyResponses.forEach(
        workingPolicyResponse -> {
          workingPolicyResponse.setInsurance_policy_type(
              EInsurance.getLabel(workingPolicyResponse.getInsurance_policy_type()));
        });
    int total = insurancePolicyRepository.getTotalInsurancePolicy(queryParam);
    return ListInsurancePolicyResponse.builder()
        .insurancePolicyResponseList(workingPolicyResponses)
        .total(total)
        .build();
  }

  @Override
  public ListLeavePolicyResponse getListLeavePolicy(QueryParam queryParam) {
    List<LeavePolicyResponse> workingPolicyResponses =
        leavePolicyRepository.getListLeavePolicy(queryParam);
    workingPolicyResponses.forEach(
        workingPolicyResponse -> {
          workingPolicyResponse.setLeave_policy_type(
              ELeavePolicy.getLabel(workingPolicyResponse.getLeave_policy_type()));
        });
    int total = leavePolicyRepository.getTotalLeavePolicy(queryParam);
    return ListLeavePolicyResponse.builder()
        .leavePolicyResponseList(workingPolicyResponses)
        .total(total)
        .build();
  }

  @Override
  public ListTaxPolicyResponse getListTaxPolicy(QueryParam queryParam) {
    List<TaxPolicyResponse> workingPolicyResponses =
        taxPolicyRepository.getListTaxPolicy(queryParam);
    workingPolicyResponses.forEach(
        workingPolicyResponse -> {
          workingPolicyResponse.setTax_policy_type(
              ETax.getLabel(workingPolicyResponse.getTax_policy_type()));
        });
    int total = taxPolicyRepository.getTotalTaxPolicy(queryParam);
    return ListTaxPolicyResponse.builder()
        .taxPolicyResponseList(workingPolicyResponses)
        .total(total)
        .build();
  }
}