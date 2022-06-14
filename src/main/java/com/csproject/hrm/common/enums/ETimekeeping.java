package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.TIMEKEEPING_STATUS_INVALID;

public enum ETimekeeping {
  WORK_LATE("Work late",1);

  private String label;
  private long value;

  ETimekeeping(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static long of(String status) {
    for (ETimekeeping eTimekeeping : ETimekeeping.values()) {
      if (eTimekeeping.label.equalsIgnoreCase(status)) {
        return eTimekeeping.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, TIMEKEEPING_STATUS_INVALID);
  }
}
