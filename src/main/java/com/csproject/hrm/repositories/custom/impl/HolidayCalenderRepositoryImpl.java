package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.HolidayCalenderRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.codegen.maven.example.tables.HolidayCalender.HOLIDAY_CALENDER;

@AllArgsConstructor
public class HolidayCalenderRepositoryImpl implements HolidayCalenderRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<HolidayCalenderDto> getAllHolidayInYear(LocalDate firstDate, LocalDate lastDate) {
    DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(HOLIDAY_CALENDER.asterisk())
        .from(HOLIDAY_CALENDER)
        .where(HOLIDAY_CALENDER.START_DATE.ge(firstDate))
        .and(HOLIDAY_CALENDER.END_DATE.le(lastDate))
        .fetchInto(HolidayCalenderDto.class);
  }
}
