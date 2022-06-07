package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.WORK_STATUS_INVALID;

public enum EWorkStatus {
  ACTIVE("Active", true),
  DEACTIVE("Deactive", false);

  private String label;
  private boolean value;

  EWorkStatus(String label, boolean value) {
    this.label = label;
    this.value = value;
  }

  public static boolean of(String workStatus) {
    for (EWorkStatus eWorkStatus : EWorkStatus.values()) {
      if (eWorkStatus.label.equalsIgnoreCase(workStatus)) {
        return eWorkStatus.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, WORK_STATUS_INVALID);
  }
}
