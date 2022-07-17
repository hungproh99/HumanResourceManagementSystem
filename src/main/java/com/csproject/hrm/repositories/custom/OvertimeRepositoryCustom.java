package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.OvertimeDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OvertimeRepositoryCustom {
  Optional<OvertimeDto> getOvertimeByEmployeeIdAndDate(LocalDate date, String employeeId);
}
