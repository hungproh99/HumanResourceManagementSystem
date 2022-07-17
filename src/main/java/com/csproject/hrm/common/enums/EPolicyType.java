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
  PAID_LEAVE("Paid Leave", 7),
  UNPAID_LEAVE("Unpaid Leave", 8),
  VPN("VPN", 9),
  FPT("FPT", 10),
  SI("SI", 11),
  HI("HI", 12),
  UI("UI", 13),
  PI("PI", 14),
  OT("OT", 15);

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