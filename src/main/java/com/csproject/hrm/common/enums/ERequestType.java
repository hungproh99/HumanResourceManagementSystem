package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.REQUEST_TYPE_INVALID;

public enum ERequestType {
  WORKING_SCHEDULE("Working Schedule", 1),
  PAIR_LEAVE("Paid Leave", 2),
  NOMINATION("Nomination", 3),
  INTEGRITY("Integrity", 4),
  COMPANY_ASSET("Company Asset", 5),
  BEHAVIOUR("Behaviour", 6),
  ADVANCE("Advance", 7),
  TAX_ENROLLMENT("Tax Enrollment", 8);

  private final String label;
  private final long value;

  ERequestType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String type) {
    for (ERequestType eRequestType : ERequestType.values()) {
      if (eRequestType.name().equalsIgnoreCase(type)) {
        return eRequestType.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, REQUEST_TYPE_INVALID);
  }
}