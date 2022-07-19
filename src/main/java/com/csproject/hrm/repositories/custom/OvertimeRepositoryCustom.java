package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.OvertimeDto;
import com.csproject.hrm.dto.response.OTDetailResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OvertimeRepositoryCustom {
  Optional<OvertimeDto> getOvertimeByEmployeeIdAndDate(LocalDate date, String employeeId);

  List<String> getListOvertimeType();

  List<OTDetailResponse> getListOTDetailResponseByEmployeeIdAndDateAndOtType(
      LocalDate startDate, LocalDate endDate, String employeeId, Long overtimeType);

  Double sumListOTDetailResponseByEmployeeIdAndDateAndOtType(
      LocalDate startDate, LocalDate endDate, String employeeId, Long overtimeType);

  Double sumListOTDetailResponseByEmployeeIdAndDate(
      LocalDate startDate, LocalDate endDate, String employeeId);
}
