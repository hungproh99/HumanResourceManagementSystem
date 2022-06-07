package com.csproject.hrm.exception.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static com.csproject.hrm.common.constant.Constants.DATE_FORMAT;

@Data
public class ErrorResponse {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
  private Date timestamp;

  private int code;

  private String status;

  private String message;

  private String stackTrace;

  private Object data;

  public ErrorResponse() {
    timestamp = new Date();
  }

  public ErrorResponse(HttpStatus httpStatus, String message) {
    this();
    this.code = httpStatus.value();
    this.status = httpStatus.name();
    this.message = message;
  }

  public ErrorResponse(HttpStatus httpStatus, String message, String stackTrace) {
    this(httpStatus, message);
    this.stackTrace = stackTrace;
  }

  public ErrorResponse(HttpStatus httpStatus, String message, String stackTrace, Object data) {
    this(httpStatus, message, stackTrace);
    this.data = data;
  }
}
