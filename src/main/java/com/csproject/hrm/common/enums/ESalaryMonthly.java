package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.REQUEST_STATUS_INVALID;

public enum ESalaryMonthly {
  PENDING("Pending", 1),
  APPROVED("Approved", 2),
  REJECTED("Rejected", 3);

  private final String label;
  private final long value;

  ESalaryMonthly(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String status) {
    for (ESalaryMonthly eSalaryMonthly : ESalaryMonthly.values()) {
      if (eSalaryMonthly.name().equalsIgnoreCase(status)) {
        return eSalaryMonthly.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_STATUS_INVALID);
  }

  public static long getValue(String status) {
    for (ESalaryMonthly eSalaryMonthly : ESalaryMonthly.values()) {
      if (eSalaryMonthly.name().equalsIgnoreCase(status)) {
        return eSalaryMonthly.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_STATUS_INVALID);
  }
}
