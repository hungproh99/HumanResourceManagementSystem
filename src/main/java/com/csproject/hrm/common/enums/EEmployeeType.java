package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.EMPLOYEE_TYPE_INVALID;

public enum EEmployeeType {
  TRAINEE("Trainee", 1),
  OFFICIAL_EMPLOYEE("Official Employee", 2),
  PROBATIONARY_STAFF("Probationary Staff", 3);

  private final String label;
  private final long value;

  EEmployeeType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String employeeType) {
    for (EEmployeeType eEmployeeType : EEmployeeType.values()) {
      if (eEmployeeType.name().equalsIgnoreCase(employeeType)) {
        return eEmployeeType.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, EMPLOYEE_TYPE_INVALID);
  }

  public static long getValue(String employeeType) {
    for (EEmployeeType eEmployeeType : EEmployeeType.values()) {
      if (eEmployeeType.name().equalsIgnoreCase(employeeType)) {
        return eEmployeeType.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, EMPLOYEE_TYPE_INVALID);
  }
}
