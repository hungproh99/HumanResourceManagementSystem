package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.AREA_INVALID;

public enum EArea {
  BACK_OFFICE("Back Office", 1);

  private String label;
  private long value;

  EArea(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static long of(String area) {
    for (EArea eArea : EArea.values()) {
      if (eArea.label.equalsIgnoreCase(area)) {
        return eArea.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, AREA_INVALID);
  }
}
