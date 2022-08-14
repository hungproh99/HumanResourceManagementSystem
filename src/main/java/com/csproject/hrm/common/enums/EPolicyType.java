package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.POLICY_TYPE_INVALID;

public enum EPolicyType {
  WORKING_TIME("Working Time", 1),
  INTEGRITY("Integrity", 2),
  COMPANY_ASSET("Company asset", 3),
  BEHAVIOUR("Behaviour", 4),
  REWARD("Reward", 5),
  ALLOWANCE("Allowance", 6),
  NOMINATION_RIGHT("Nomination right", 7),
  PAID_LEAVE("Paid Leave", 8),
  UNPAID_LEAVE("Unpaid Leave", 9),
  TAX("Tax", 10),
  INSURANCE("Insurance", 11),
  OT("OT", 12),
  ADVANCE("Advance", 13);

  private final String label;
  private final long value;

  EPolicyType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String policyType) {
    for (EPolicyType ePolicyType : EPolicyType.values()) {
      if (ePolicyType.name().equalsIgnoreCase(policyType)) {
        return ePolicyType.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, POLICY_TYPE_INVALID);
  }
}