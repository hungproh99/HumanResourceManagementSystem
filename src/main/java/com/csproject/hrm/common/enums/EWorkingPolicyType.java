package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.WORKING_POLICY_TYPE_INVALID;

public enum EWorkingPolicyType {
  WORKING_TIME("Working Time", 1),
  INTEGRITY("Integrity", 2),
  COMPANY_ASSET("Company asset", 3),
  BEHAVIOUR("Company asset", 4);

  private String label;
  private long value;

  EWorkingPolicyType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String workingPolicyType) {
    for (EWorkingPolicyType eWorkingPolicyType : EWorkingPolicyType.values()) {
      if (eWorkingPolicyType.name().equalsIgnoreCase(workingPolicyType)) {
        return eWorkingPolicyType.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, WORKING_POLICY_TYPE_INVALID);
  }
}
