package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.jooq.QueryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

public interface SalaryMonthlyService {
  SalaryMonthlyResponseList getAllSalaryMonthly(
      QueryParam queryParam, String employeeId, boolean isManager);

  SalaryMonthlyDetailResponse getSalaryMonthlyDetailBySalaryMonthlyId(Long salaryMonthlyId);

  void upsertSalaryMonthlyByEmployeeIdList(LocalDate startDate, LocalDate endDate);

  void exportSalaryMonthlyToCsv(Writer writer, QueryParam queryParam, List<Long> list);

  void exportSalaryMonthlyExcel(
      HttpServletResponse response, QueryParam queryParam, List<Long> list);
}
