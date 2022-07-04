package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.LeavePolicyResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface LeavePolicyRepositoryCustom {
  List<LeavePolicyResponse> getListLeavePolicy(QueryParam queryParam);

  int getTotalLeavePolicy(QueryParam queryParam);
}