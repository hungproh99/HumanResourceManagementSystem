package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.EmployeeInsuranceResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.EmployeeInsuranceRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.codegen.maven.example.tables.EmployeeInsurance.EMPLOYEE_INSURANCE;
import static org.jooq.codegen.maven.example.tables.PolicyType.POLICY_TYPE;

@AllArgsConstructor
public class EmployeeInsuranceRepositoryImpl implements EmployeeInsuranceRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<EmployeeInsuranceResponse> getListInsuranceByEmployeeId(String employeeId) {
    DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE_INSURANCE.EMPLOYEE_INSURANCE_ID, POLICY_TYPE.POLICY_TYPE_.as("policy_type"))
        .from(EMPLOYEE_INSURANCE)
        .leftJoin(POLICY_TYPE)
        .on(POLICY_TYPE.POLICY_TYPE_ID.eq(EMPLOYEE_INSURANCE.POLICY_TYPE_ID))
        .where(EMPLOYEE_INSURANCE.EMPLOYEE_ID.eq(employeeId))
        .fetchInto(EmployeeInsuranceResponse.class);
  }
}