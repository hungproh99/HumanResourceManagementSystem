package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.CheckInCheckOutResponse;
import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponses;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimekeepingRepositoryCustom {
  List<TimekeepingResponses> getListAllTimekeeping(QueryParam queryParam);

  List<TimekeepingResponses> getListTimekeepingToExport(QueryParam queryParam, List<String> list);

  Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, LocalDate date);

  List<CheckInCheckOutResponse> getCheckInCheckOutByTimekeepingID(Long timekeepingID);

  int countListAllTimekeeping(QueryParam queryParam);
}
