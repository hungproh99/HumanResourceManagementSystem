package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.COMPENSATION_BENEFIT_POLICY_TYPE_INVALID;

public enum ECompensationBenefitPolicyType {
  REWARD("Reward", 1),
  ALLOWANCE("Allowance", 2);

  private String label;
  private long value;

  ECompensationBenefitPolicyType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String compensationBenefitPolicyType) {
    for (ECompensationBenefitPolicyType eCompensationBenefitPolicyType :
        ECompensationBenefitPolicyType.values()) {
      if (eCompensationBenefitPolicyType.name().equalsIgnoreCase(compensationBenefitPolicyType)) {
        return eCompensationBenefitPolicyType.label;
      }
    }
    throw new CustomErrorException(
        HttpStatus.BAD_REQUEST, COMPENSATION_BENEFIT_POLICY_TYPE_INVALID);
  }
}
