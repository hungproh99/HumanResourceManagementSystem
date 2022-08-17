package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.POLICY_NAME_INVALID;

public enum EPolicyName {
  LEAVE_SOON("Leave Soon", 1),
  WORK_LATE("Work Late", 2),
  HI("Health Insurance", 3),
  SI("Social Insurance", 4),
  UI("Unemployment Insurance", 5),
  VNP("Viet Nam Progressive", 6),
  TRANSPORTATION_ALLOWANCE("Transportation Allowance", 7),
  PHONE_ALLOWANCE("Phone Allowance", 8),
  MEAL_ALLOWANCE("Meal Allowance", 9);

  private final String label;
  private final long value;

  EPolicyName(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String policyName) {
    for (EPolicyName ePolicyName : EPolicyName.values()) {
      if (ePolicyName.name().equalsIgnoreCase(policyName)) {
        return ePolicyName.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, POLICY_NAME_INVALID);
  }

  public static long getValue(String policyName) {
    for (EPolicyName ePolicyName : EPolicyName.values()) {
      if (ePolicyName.name().equalsIgnoreCase(policyName)) {
        return ePolicyName.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, POLICY_NAME_INVALID);
  }
}
