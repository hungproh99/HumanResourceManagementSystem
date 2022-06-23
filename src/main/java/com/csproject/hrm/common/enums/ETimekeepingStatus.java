package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.TIMEKEEPING_STATUS_INVALID;

public enum ETimekeepingStatus {
  WORK_LATE("Late", 1),
  LEAVE_SOON("Soon", 2),
  DAY_OFF("Off", 3),
  PAID_LEAVE("PL", 4),
  NORMAL("Normal", 5),
  HAFT_DAY("Haft", 6),
  HOLIDAY("Holiday", 7),
  OVERTIME("Overtime", 8);

  private String label;
  private long value;

  ETimekeepingStatus(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getValue(String status) {
    for (ETimekeepingStatus eTimekeepingStatus : ETimekeepingStatus.values()) {
      if (eTimekeepingStatus.name().equalsIgnoreCase(status)) {
        return eTimekeepingStatus.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, TIMEKEEPING_STATUS_INVALID);
  }
}
