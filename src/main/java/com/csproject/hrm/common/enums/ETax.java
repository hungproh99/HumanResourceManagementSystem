package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.TAX_INVALID;

public enum ETax {
  VPN("VPN", 1),
  FPT("FPT", 2);
  private final String label;
  private final long value;

  ETax(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String tax) {
    for (ETax eTax : ETax.values()) {
      if (eTax.name().equalsIgnoreCase(tax)) {
        return eTax.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, TAX_INVALID);
  }
}
