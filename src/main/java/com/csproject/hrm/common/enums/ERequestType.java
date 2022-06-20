package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.REQUEST_TYPE_INVALID;

public enum ERequestType {
  DAY_WORK("Day work", 1);

  private String label;
  private long value;

  ERequestType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static long of(String type) {
    for (ERequestType eRequestType : ERequestType.values()) {
      if (eRequestType.label.equalsIgnoreCase(type)) {
        return eRequestType.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_TYPE_INVALID);
  }
}
