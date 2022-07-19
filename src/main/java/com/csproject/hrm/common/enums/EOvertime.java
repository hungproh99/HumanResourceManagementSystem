package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.OVER_TIME_INVALID;

public enum EOvertime {
  IN_WEEK("In Week", 1),
  WEEKEND("Weekend", 2),
  HOLIDAY("Holiday", 3);

  private String label;
  private long value;

  EOvertime(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String overtime) {
    for (EOvertime eOvertime : EOvertime.values()) {
      if (eOvertime.name().equalsIgnoreCase(overtime)) {
        return eOvertime.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, OVER_TIME_INVALID);
  }

  public static long getValue(String overtime) {
    for (EOvertime eOvertime : EOvertime.values()) {
      if (eOvertime.name().equalsIgnoreCase(overtime)) {
        return eOvertime.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, OVER_TIME_INVALID);
  }
}
