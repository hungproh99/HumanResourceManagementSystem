package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.JOB_INVALID;

public enum EJob {
  DEV1_IT("Develop 1", "IT", 1);

  private final String grade;
  private final String position;
  private final long value;

  EJob(String grade, String position, long value) {
    this.grade = grade;
    this.position = position;
    this.value = value;
  }

  public static long of(String grade, String position) {
    for (EJob eJob : EJob.values()) {
      if (eJob.grade.equalsIgnoreCase(grade) && eJob.position.equalsIgnoreCase(position)) {
        return eJob.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, JOB_INVALID);
  }
}
