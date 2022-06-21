package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.jooq.QueryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

public interface TimekeepingService {
  List<TimekeepingResponse> getListAllTimekeeping(QueryParam queryParam);

  void exportTimekeepingToCsv(Writer writer, List<String> list);

  void exportTimekeepingToExcel(HttpServletResponse response, List<String> list);

  Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, String date);
}