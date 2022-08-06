package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponsesList;
import com.csproject.hrm.jooq.QueryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimekeepingService {
  TimekeepingResponsesList getListTimekeepingByManagement(QueryParam queryParam, String employeeId);

  TimekeepingResponsesList getListTimekeepingByEmployeeID(QueryParam queryParam);

  void exportTimekeepingToCsv(Writer writer, QueryParam queryParam, List<String> list);

  void exportTimekeepingToExcel(
      HttpServletResponse response, QueryParam queryParam, List<String> list);

  Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, String date);

  void upsertPointPerDay(LocalDate currentDate);

  void insertTimekeepingCheckInCheckOut(
      String employeeId, LocalDate localDate, LocalTime localTime);
}