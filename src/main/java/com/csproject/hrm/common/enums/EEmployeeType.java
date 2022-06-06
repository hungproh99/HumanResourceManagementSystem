package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.EMPLOYEE_TYPE_INVALID;

public enum EEmployeeType {
  FULL_TIME("Full Time", 1),
  PART_TIME("Part Time", 2);

  private String label;
  private long value;

  EEmployeeType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static long of(String employeeType) {
    for (EEmployeeType eEmployeeType : EEmployeeType.values()) {
      if (eEmployeeType.label.equalsIgnoreCase(employeeType)) {
        return eEmployeeType.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, EMPLOYEE_TYPE_INVALID);
  }
}
