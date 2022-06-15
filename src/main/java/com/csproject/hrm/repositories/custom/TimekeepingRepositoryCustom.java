package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimekeepingRepositoryCustom {
  List<TimekeepingResponse> getListAllTimekeeping(QueryParam queryParam);

  List<TimekeepingResponse> getListTimekeepingToExport(List<String> list);
}
