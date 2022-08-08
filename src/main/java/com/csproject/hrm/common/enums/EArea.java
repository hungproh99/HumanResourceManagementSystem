package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.AREA_INVALID;

public enum EArea {
  BACK_OFFICE("Back Office", 1),
  SALES("Sales", 2),
  MARKETING("Marketing", 3),
  ACCOUNTANT("Accountant", 4),
  HR("HR", 5);

  private final String label;
  private final long value;

  EArea(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String area) {
    for (EArea eArea : EArea.values()) {
      if (eArea.name().equalsIgnoreCase(area)) {
        return eArea.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, AREA_INVALID);
  }

  public static long getValue(String area) {
    for (EArea eArea : EArea.values()) {
      if (eArea.name().equalsIgnoreCase(area)) {
        return eArea.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, AREA_INVALID);
  }
}