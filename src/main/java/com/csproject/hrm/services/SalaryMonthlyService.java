package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.jooq.QueryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

public interface SalaryMonthlyService {
  SalaryMonthlyResponseList getAllSalaryMonthlyForPersonal(
      QueryParam queryParam, String employeeId);

  SalaryMonthlyResponseList getAllSalaryMonthlyForManagement(
      QueryParam queryParam, String employeeId);

  SalaryMonthlyDetailResponse getSalaryMonthlyDetailBySalaryMonthlyId(Long salaryMonthlyId);

  void upsertSalaryMonthlyByEmployeeIdList(LocalDate startDate, LocalDate endDate);

  void exportPersonalSalaryMonthlyToCsv(
      Writer writer, QueryParam queryParam, List<Long> list, String employeeId);

  void exportPersonalSalaryMonthlyExcel(
      HttpServletResponse response, QueryParam queryParam, List<Long> list, String employeeId);

  void exportManagementSalaryMonthlyToCsv(
      Writer writer, QueryParam queryParam, List<Long> list, String employeeId);

  void exportManagementSalaryMonthlyExcel(
      HttpServletResponse response, QueryParam queryParam, List<Long> list, String employeeId);

  void updateRejectSalaryMonthly(RejectSalaryMonthlyRequest rejectSalaryMonthlyRequest);

  void updateApproveSalaryMonthly(Long salaryMonthlyId);

  void updateCheckedSalaryMonthly(
      UpdateSalaryMonthlyRequest updateSalaryMonthlyRequest, String employeeId);

  void updateDeductionSalary(DeductionSalaryRequest deductionSalaryRequest);

  void deleteDeductionSalary(Long deductionSalaryId);

  void updateBonusSalary(BonusSalaryRequest bonusSalaryRequest);

  void deleteBonusSalary(Long bonusSalaryId);

  void updateAdvanceSalary(AdvanceSalaryRequest advanceSalaryRequest);

  void deleteAdvanceSalary(Long advanceSalaryId);

  void updateAllSalaryRemind(LocalDate checkDate);

  List<DeductionTypeDto> getListDeductionTypeDto();

  List<BonusTypeDto> getListBonusTypeDto();
}
