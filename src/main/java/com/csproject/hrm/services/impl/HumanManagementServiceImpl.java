package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.response.HrmResponse;

import java.util.List;

public interface HumanManagementServiceImpl {
    List<HrmResponse> getListHumanResource(String offset, String limit);
}
