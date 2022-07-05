package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryMonthlyRepositoryCustom {
  List<SalaryMonthlyResponse> getAllSalaryMonthly(QueryParam queryParam);
}
