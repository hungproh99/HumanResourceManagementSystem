package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.jooq.QueryParam;

import java.time.LocalDate;
import java.util.List;

public interface SalaryMonthlyService {
  SalaryMonthlyResponseList getAllSalaryMonthly(
      QueryParam queryParam, String employeeId, boolean isManager);

  SalaryMonthlyDetailResponse getSalaryMonthlyDetailByEmployeeId(
      String employeeId, LocalDate startDate, LocalDate endDate);

  void upsertSalaryMonthlyByEmployeeIdList(
      List<String> employeeIdList, LocalDate startDate, LocalDate endDate);
}
