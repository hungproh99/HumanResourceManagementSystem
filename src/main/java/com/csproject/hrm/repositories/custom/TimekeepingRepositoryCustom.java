package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.CheckInCheckOutResponse;
import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimekeepingRepositoryCustom {
  List<TimekeepingResponse> getListAllTimekeeping(QueryParam queryParam);

  List<TimekeepingResponse> getListTimekeepingToExport(QueryParam queryParam, List<String> list);

  List<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(String employeeID, LocalDate date);
  
  List<CheckInCheckOutResponse> getCheckInCheckOutByTimekeepingID(Long timekeepingID);
}