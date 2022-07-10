package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.DEDUCTION_INVALID;

public enum EDeduction {
  LATE_WORK("Late work", 1);

  private String label;
  private long value;

  EDeduction(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String deduction) {
    for (EDeduction eDeduction : EDeduction.values()) {
      if (eDeduction.name().equalsIgnoreCase(deduction)) {
        return eDeduction.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, DEDUCTION_INVALID);
  }
}
