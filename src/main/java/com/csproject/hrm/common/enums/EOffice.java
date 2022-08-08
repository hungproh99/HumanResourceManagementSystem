package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.OFFICE_INVALID;

public enum EOffice {
  HN_OFFICE("Hà Nội Office", 1),
  DN_OFFICE("Đà Nẵng Office", 2),
  HCM_OFFICE("Hồ Chí Minh Office", 3);

  private String label;
  private long value;

  EOffice(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String office) {
    for (EOffice eOffice : EOffice.values()) {
      if (eOffice.name().equalsIgnoreCase(office)) {
        return eOffice.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, OFFICE_INVALID);
  }

  public static long getValue(String office) {
    for (EOffice eOffice : EOffice.values()) {
      if (eOffice.name().equalsIgnoreCase(office)) {
        return eOffice.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, OFFICE_INVALID);
  }
}
