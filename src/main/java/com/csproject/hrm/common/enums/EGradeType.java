package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.GRADE_TYPE_INVALID;

public enum EGradeType {
  DEVELOP_1("Develop 1", 1),
  DEVELOP_2("Develop 2", 2),
  MARKETING_1("Marketing 1", 3),
  MARKETING_2("Marketing 2", 4),
  SALE_1("Sale 1", 5),
  SALE_2("Sale 2", 6);

  private final String label;
  private final long value;

  EGradeType(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String grade) {
    for (EGradeType eGradeType : EGradeType.values()) {
      if (eGradeType.name().equalsIgnoreCase(grade)) {
        return eGradeType.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, GRADE_TYPE_INVALID);
  }

  public static long getValue(String grade) {
    for (EGradeType eGradeType : EGradeType.values()) {
      if (eGradeType.name().equalsIgnoreCase(grade)) {
        return eGradeType.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, GRADE_TYPE_INVALID);
  }
}