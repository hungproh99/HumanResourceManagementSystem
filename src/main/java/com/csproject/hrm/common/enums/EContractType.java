package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.CONTRACT_TYPE_INVALID;

public enum EContractType {
  FULL_TIME("Full Time", 1),
  PART_TIME("Part Time", 2);

  private final String label;
  private final long value;

  EContractType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static long of(String role) {
    for (EContractType contractType : EContractType.values()) {
      if (contractType.label.equalsIgnoreCase(role)) {
        return contractType.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, CONTRACT_TYPE_INVALID);
  }
}