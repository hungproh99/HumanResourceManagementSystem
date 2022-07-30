package com.csproject.hrm.services;

import java.time.LocalDate;
import java.util.List;

public interface HolidayCalenderService {
  List<LocalDate> getAllHolidayByYear(LocalDate currDate);
}