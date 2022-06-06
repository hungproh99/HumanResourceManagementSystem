package com.csproject.hrm.jooq;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.ORDER_BY_INVALID;

public enum OrderBy {
  ASC,
  DESC;

  public static OrderBy of(final String orderBy) {
    try {
      return OrderBy.valueOf(orderBy.toUpperCase());
    } catch (RuntimeException re) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
    }
  }
}