package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.dto.request.HolidayCalendarRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayCalenderRepositoryCustom {
  List<HolidayCalenderDto> getAllHolidayInYear(LocalDate firstDate, LocalDate lastDate);

  void insertHoliday(HolidayCalendarRequest holidayCalendarRequest);
}
