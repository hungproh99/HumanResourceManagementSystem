package com.csproject.hrm.common.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static Date convert(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }

    return Date.valueOf(localDate);
  }

  public static LocalDate convert(String date) {

    if (date == null) {
      return null;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(date, formatter);
  }

  public static LocalDate convert(Date date) {

    if (date == null) {
      return null;
    }

    return date.toLocalDate();
  }
}
