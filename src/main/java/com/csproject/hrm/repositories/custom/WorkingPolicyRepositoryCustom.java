package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.WorkingPolicyResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface WorkingPolicyRepositoryCustom {
  List<WorkingPolicyResponse> getListWorkingPolicy(QueryParam queryParam);

  int getTotalWorkingPolicy(QueryParam queryParam);
}