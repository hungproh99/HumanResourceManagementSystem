package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.INSURANCE_INVALID;

public enum EInsurance {
  VPN("VPN", 1),
  FPT("FPT", 2);
  private final String label;
  private final long value;

  EInsurance(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String insurance) {
    for (EInsurance eInsurance : EInsurance.values()) {
      if (eInsurance.name().equalsIgnoreCase(insurance)) {
        return eInsurance.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, INSURANCE_INVALID);
  }
}
