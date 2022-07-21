package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.AdvanceSalaryDto;
import com.csproject.hrm.dto.dto.BonusSalaryDto;
import com.csproject.hrm.dto.dto.DeductionSalaryDto;
import com.csproject.hrm.dto.dto.UpdateStatusSalaryMonthlyDto;
import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.jooq.QueryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

public interface SalaryMonthlyService {
  SalaryMonthlyResponseList getAllSalaryMonthly(
      QueryParam queryParam, String employeeId, String role);

  SalaryMonthlyDetailResponse getSalaryMonthlyDetailBySalaryMonthlyId(Long salaryMonthlyId);

  void upsertSalaryMonthlyByEmployeeIdList(LocalDate startDate, LocalDate endDate);

  void exportSalaryMonthlyToCsv(Writer writer, QueryParam queryParam, List<Long> list);

  void exportSalaryMonthlyExcel(
      HttpServletResponse response, QueryParam queryParam, List<Long> list);

  void updateStatusSalaryMonthly(UpdateStatusSalaryMonthlyDto updateStatusSalaryMonthlyDto);

  void updateDeductionSalary(DeductionSalaryDto deductionSalaryDto);

  void deleteDeductionSalary(Long deductionSalaryId);

  void updateBonusSalary(BonusSalaryDto bonusSalaryDto);

  void deleteBonusSalary(Long bonusSalaryId);

  void updateAdvanceSalary(AdvanceSalaryDto advanceSalaryDto);

  void deleteAdvanceSalary(Long advanceSalaryId);
}
