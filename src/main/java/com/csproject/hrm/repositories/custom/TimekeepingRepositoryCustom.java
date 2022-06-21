package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimekeepingRepositoryCustom {
  List<TimekeepingResponse> getListAllTimekeeping(QueryParam queryParam);

  List<TimekeepingResponse> getListTimekeepingToExport(List<String> list);

  Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, LocalDate date);

  List<CheckInCheckOutResponse> getCheckInCheckOutByTimekeepingID(Long timekeepingID);
}