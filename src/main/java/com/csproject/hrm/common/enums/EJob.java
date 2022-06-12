package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.JOB_INVALID;

public enum EJob {
  IT("IT", 1);
  private final String position;
  private final long value;

  EJob(String position, long value) {
    this.position = position;
    this.value = value;
  }

  public static long of(String position) {
    for (EJob eJob : EJob.values()) {
      if (eJob.position.equalsIgnoreCase(position)) {
        return eJob.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, JOB_INVALID);
  }
}