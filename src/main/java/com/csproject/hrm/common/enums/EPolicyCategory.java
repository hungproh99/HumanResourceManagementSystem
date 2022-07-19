package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.POLICY_CATEGORY_INVALID;

public enum EPolicyCategory {
  WORKING_RULE("Working Rule", 1),
  COMPENSATION_BENEFIT_POLICY("Compensation Benefit Policy", 2),
  TAX_POLICY("Tax Policy", 3),
  INSURANCE_POLICY("Insurance Policy", 4),
  LEAVE_POLICY("Leave Policy", 5),
  SALARY_POLICY("Salary Policy", 6),
  OVER_TIME("Overtime", 7);

  private String label;
  private long value;

  EPolicyCategory(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String policyCategory) {
    for (EPolicyCategory ePolicyCategory : EPolicyCategory.values()) {
      if (ePolicyCategory.name().equalsIgnoreCase(policyCategory)) {
        return ePolicyCategory.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, POLICY_CATEGORY_INVALID);
  }
}
