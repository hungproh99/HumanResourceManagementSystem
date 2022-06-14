package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.TimekeepingRepository;
import com.csproject.hrm.services.TimekeepingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TimekeepingServiceImpl implements TimekeepingService {
  @Autowired TimekeepingRepository timekeepingRepository;

  @Override
  public List<TimekeepingResponse> getListAllTimekeeping(QueryParam queryParam) {
    return timekeepingRepository.getListAllTimekeeping(queryParam);
  }
}
