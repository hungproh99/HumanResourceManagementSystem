package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface HumanManagementServiceImpl {
    List<HrmResponse> getListHumanResource(QueryParam queryParam);
}
