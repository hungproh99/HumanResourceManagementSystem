package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.BONUS_INVALID;

public enum EInsurance {
  HI("Health Insurance", 1),
  SI("Social Insurance", 1),
  UI("Unemployment Insurance", 1);

  private final String label;
  private final long value;

  EInsurance(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String bonus) {
    for (EInsurance eBonus : EInsurance.values()) {
      if (eBonus.name().equalsIgnoreCase(bonus)) {
        return eBonus.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, BONUS_INVALID);
  }
}