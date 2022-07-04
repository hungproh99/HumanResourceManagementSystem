package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.LEAVE_POLICY_TYPE_INVALID;

public enum ELeavePolicy {
  PAID_LEAVE("Paid Leave", 1),
  UNPAID_LEAVE("Unpaid Leave", 2);
  private String label;
  private long value;

  ELeavePolicy(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String leavePolicy) {
    for (ELeavePolicy eLeavePolicy : ELeavePolicy.values()) {
      if (eLeavePolicy.name().equalsIgnoreCase(leavePolicy)) {
        return eLeavePolicy.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, LEAVE_POLICY_TYPE_INVALID);
  }
}
