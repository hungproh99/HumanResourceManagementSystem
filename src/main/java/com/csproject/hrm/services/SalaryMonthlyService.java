package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface SalaryMonthlyService {
  SalaryMonthlyDetailResponse getSalaryMonthlyDetailByEmployeeId(
      String employeeId, LocalDate startDate, LocalDate endDate);

  void insertSalaryMonthlyByEmployeeIdList(
      List<String> employeeIdList, LocalDate startDate, LocalDate endDate);
}
