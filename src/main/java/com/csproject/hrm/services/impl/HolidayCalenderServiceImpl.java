package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.dto.request.HolidayCalendarRequest;
import com.csproject.hrm.repositories.HolidayCalenderRepository;
import com.csproject.hrm.services.HolidayCalenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class HolidayCalenderServiceImpl implements HolidayCalenderService {
  @Autowired HolidayCalenderRepository holidayCalenderRepository;

  @Override
  public List<HolidayCalenderDto> getAllHolidayByYear(LocalDate currDate) {
    LocalDate firstDate = currDate.with(firstDayOfYear());
    LocalDate lastDate = currDate.with(lastDayOfYear());

    return holidayCalenderRepository.getAllHolidayInYear(firstDate, lastDate);
  }

  @Override
  public void insertHolidayCalendar(HolidayCalendarRequest holidayCalendarRequest) {
    holidayCalenderRepository.insertHoliday(holidayCalendarRequest);
  }
}