package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalaryMonthlyRepositoryCustom {
  List<SalaryMonthlyDetailResponse> getAllSalaryMonthly(QueryParam queryParam, String managerId);

  Long getSalaryMonthlyIdByEmployeeIdAndDate(
      String employeeId, LocalDate startDate, LocalDate endDate);

  void insertSalaryMonthlyByListEmployee(List<SalaryMonthlyResponse> salaryMonthlyResponseList);
}
