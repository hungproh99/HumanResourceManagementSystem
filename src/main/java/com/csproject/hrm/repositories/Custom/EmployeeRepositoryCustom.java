package com.csproject.hrm.repositories.Custom;

import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepositoryCustom {
  List<HrmResponse> findAllEmployee(QueryParam queryParam);

  void insertEmployee(HrmPojo hrmPojo);

  int countEmployeeSameStartName(String fullName);

  int countAllEmployeeByCondition(QueryParam queryParam);
}
