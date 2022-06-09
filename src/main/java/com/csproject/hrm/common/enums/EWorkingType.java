package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.WORKING_TYPE_INVALID;

public enum EWorkingType {
  FULL_TIME("Full Time", 1),
  PART_TIME("Part Time", 2);

  private String label;
  private long value;

  EWorkingType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static long of(String workingType) {
    for (EWorkingType eWorkingType : EWorkingType.values()) {
      if (eWorkingType.label.equalsIgnoreCase(workingType)) {
        return eWorkingType.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, WORKING_TYPE_INVALID);
  }
}