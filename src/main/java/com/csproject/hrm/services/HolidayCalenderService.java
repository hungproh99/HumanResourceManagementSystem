package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.dto.request.HolidayCalendarRequest;

import java.time.LocalDate;
import java.util.List;

public interface HolidayCalenderService {
  List<HolidayCalenderDto> getAllHolidayByYear(LocalDate currDate);

  void insertHolidayCalendar(HolidayCalendarRequest holidayCalendarRequest);
}