package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.JOB_INVALID;

public enum EJob {
  IT("IT", "Develop 1", 1);

  private String grade;
  private String position;
  private long value;

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
