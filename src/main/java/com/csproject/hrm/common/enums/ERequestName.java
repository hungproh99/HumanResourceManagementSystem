package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.REQUEST_NAME_INVALID;

public enum ERequestName {
  LEAVE_SOON("Leave Soon", 1),
  WORK_LATE("Work Late", 14),
  OT("OT", 2),
  PAID_LEAVE("Paid Leave", 6),
  ADVANCES("Advances", 12),
  PROMOTION("Promotion", 3),
  SALARY_INCREMENT("Salary increment", 4),
  BONUS("Bonus", 5),
  LEAKING_SECURED_INFORMATION("Leaking secured information", 7),
  COMPANY_ASSET("Company asset", 8),
  CONFLICT_WITH_CUSTOMER("Conflict with customer", 9),
  CONFLICT_WITH_INTERNAL_COMPANY("Conflict in internal company", 10),
  WORKING_ENVIRONMENT("Working Environment", 11),
  TAX_ENROLLMENT("Tax Enrollment", 13);

  private final String label;
  private final long value;

  ERequestName(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String name) {
    for (ERequestName eRequestName : ERequestName.values()) {
      if (eRequestName.name().equalsIgnoreCase(name)) {
        return eRequestName.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_NAME_INVALID);
  }
}