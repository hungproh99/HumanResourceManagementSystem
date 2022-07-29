package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.SalaryMonthlyDto;
import com.csproject.hrm.dto.request.RejectSalaryMonthlyRequest;
import com.csproject.hrm.dto.request.UpdateSalaryMonthlyRequest;
import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryMonthlyRepositoryCustom {
  SalaryMonthlyResponseList getAllPersonalSalaryMonthly(QueryParam queryParam, String employeeId);

  SalaryMonthlyResponseList getAllManagementSalaryMonthly(
      QueryParam queryParam, String employeeId, String isEnoughLevel);

  Optional<SalaryMonthlyResponse> getSalaryMonthlyBySalaryId(Long salaryId);

  Long getSalaryMonthlyIdByEmployeeIdAndDate(
      String employeeId, LocalDate startDate, LocalDate endDate, String salaryStatus);

  void updateSalaryMonthlyByListEmployee(List<SalaryMonthlyDto> salaryMonthlyDtoList);

  List<SalaryMonthlyResponse> getListPersonalSalaryMonthlyToExport(
      QueryParam queryParam, List<Long> list, String employeeId);

  List<SalaryMonthlyResponse> getListManagementSalaryMonthlyToExport(
      QueryParam queryParam, List<Long> list, String employeeId);

  void updateStatusSalaryMonthlyBySalaryMonthlyId(Long salaryMonthlyId, String statusSalary);

  void updateCheckedSalaryMonthly(
      UpdateSalaryMonthlyRequest updateSalaryMonthlyRequest, String employeeId);

  void updateRejectSalaryMonthly(RejectSalaryMonthlyRequest rejectSalaryMonthlyRequest);

  boolean checkExistSalaryMonthly(Long salaryMonthlyId);
}
