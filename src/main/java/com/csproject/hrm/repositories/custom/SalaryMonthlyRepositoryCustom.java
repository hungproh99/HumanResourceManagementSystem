package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.SalaryMonthlyDto;
import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryMonthlyRepositoryCustom {
  SalaryMonthlyResponseList getAllSalaryMonthly(
      QueryParam queryParam, List<String> employeeIdList, boolean isManager);

  Optional<SalaryMonthlyResponse> getSalaryMonthlyBySalaryId(Long salaryId);

  Long getSalaryMonthlyIdByEmployeeIdAndDate(
      String employeeId, LocalDate startDate, LocalDate endDate, String salaryStatus);

  void updateSalaryMonthlyByListEmployee(List<SalaryMonthlyDto> salaryMonthlyDtoList);

  List<SalaryMonthlyResponse> getListSalaryMonthlyToExport(
      QueryParam queryParam, List<Long> list);
}
