package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

public enum ComparisonOperator {
  AEQ,
  AGE,
  AGT,
  ALE,
  ALT,
  ANE,
  ABT,
  ALK,
  AIN,
  ANN,
  ANL;

  public static ComparisonOperator of(final String filter) {
    try {
      return ComparisonOperator.valueOf(filter.toUpperCase());
    } catch (RuntimeException re) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Filter");
    }
  }
}