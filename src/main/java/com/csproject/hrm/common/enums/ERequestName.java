package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.REQUEST_NAME_INVALID;

public enum ERequestName {
  PAID_LEAVE("Paid Leave", 1);

  private String label;
  private long value;

  ERequestName(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static long of(String name) {
    for (ERequestName eRequestName : ERequestName.values()) {
      if (eRequestName.label.equalsIgnoreCase(name)) {
        return eRequestName.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_NAME_INVALID);
  }
}
