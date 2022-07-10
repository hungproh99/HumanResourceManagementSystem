package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkingPlaceRepositoryCustom {
  List<OfficeDto> getListOffice();

  List<AreaDto> getListArea();

  List<JobDto> getListPosition();

  List<GradeDto> getListGradeByPosition(Long jodId);

  void insertNewWorkingPlace(
      String employeeId,
      Long area,
      Long office,
      Long grade,
      Long position,
      LocalDate startDate,
      boolean oldStatus,
      boolean newStatus);
}
