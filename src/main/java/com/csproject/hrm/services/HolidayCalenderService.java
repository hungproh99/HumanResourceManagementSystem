package com.csproject.hrm.services;

import com.csproject.hrm.dto.request.HolidayCalendarRequest;

import java.time.LocalDate;
import java.util.List;

public interface HolidayCalenderService {
  List<LocalDate> getAllHolidayByYear(LocalDate currDate);

  void insertHolidayCalendar(HolidayCalendarRequest holidayCalendarRequest);
}