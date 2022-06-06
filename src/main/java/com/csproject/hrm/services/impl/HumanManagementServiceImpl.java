package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface HumanManagementServiceImpl {
  HrmResponseList getListHumanResource(QueryParam queryParam);

  void insertEmployee(HrmRequest hrmRequest);

  void insertMultiEmployee(List<HrmRequest> hrmRequestList);
}
