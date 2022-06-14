package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Service;

import java.io.Writer;
import java.util.List;

public interface TimekeepingService {
  List<TimekeepingResponse> getListAllTimekeeping(QueryParam queryParam);

  void exportTimekeepingToCsv(Writer writer, List<String> list);
}
