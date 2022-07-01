package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.REQUEST_STATUS_INVALID;

public enum ERequestStatus {
  PENDING("Pending", 1);

  private String label;
  private long value;

  ERequestStatus(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String status) {
    for (ERequestStatus eRequestStatus : ERequestStatus.values()) {
      if (eRequestStatus.name().equalsIgnoreCase(status)) {
        return eRequestStatus.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_STATUS_INVALID);
  }

  public static long getValue(String status) {
    for (ERequestStatus eRequestStatus : ERequestStatus.values()) {
      if (eRequestStatus.label.equalsIgnoreCase(status)) {
        return eRequestStatus.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_STATUS_INVALID);
  }
}
