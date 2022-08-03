package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

public enum EPaidLeaveReason {
  FAMILY("Family Reason", 1),
  UNEXPECTED("Unexpected Reason", 2),
  HEALTH("Health Reason", 3),
  OTHER("Other", 4);

  private final String label;
  private final long value;

  EPaidLeaveReason(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String label) {
    for (EPaidLeaveReason paidLeaveReason : EPaidLeaveReason.values()) {
      if (paidLeaveReason.name().equalsIgnoreCase(label)) {
        return paidLeaveReason.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Paid Leave Reason");
  }
}