package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.TIMEKEEPING_STATUS_INVALID;

public enum ETimekeepingStatus {
  WORK_LATE("late", 1),
  LEAVE_SOON("soon", 2),
  DAY_OFF("off", 3),
  PAID_LEAVE("PL", 4),
  NORMAL("normal", 5),
  HAFT_DAY("haft", 6),
  HOLIDAY("holiday", 7),
  OVERTIME("overtime", 8);

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
