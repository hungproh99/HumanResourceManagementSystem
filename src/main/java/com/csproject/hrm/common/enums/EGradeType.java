package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.GRADE_TYPE_INVALID;

public enum EGradeType {
  DEVELOP_1("Develop 1", 1),
  DEVELOP_2("Develop 2", 2);

  private String label;
  private long value;

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
}
