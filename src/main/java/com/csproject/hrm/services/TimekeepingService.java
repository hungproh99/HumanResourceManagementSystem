package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.dto.response.TimekeepingResponseList;
import com.csproject.hrm.jooq.QueryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

public interface TimekeepingService {
  List<TimekeepingResponseList> getListAllTimekeeping(QueryParam queryParam);

  void exportTimekeepingToCsv(Writer writer, QueryParam queryParam, List<String> list);

  void exportTimekeepingToExcel(
      HttpServletResponse response, QueryParam queryParam, List<String> list);

  Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, String date);
}