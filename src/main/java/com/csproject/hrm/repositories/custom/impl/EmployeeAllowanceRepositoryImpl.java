package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.EmployeeAllowanceResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.EmployeeAllowanceRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.codegen.maven.example.Tables.EMPLOYEE_ALLOWANCE;
import static org.jooq.codegen.maven.example.Tables.POLICY_NAME;
import static org.jooq.codegen.maven.example.tables.PolicyType.POLICY_TYPE;

@AllArgsConstructor
public class EmployeeAllowanceRepositoryImpl implements EmployeeAllowanceRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<EmployeeAllowanceResponse> getListAllowanceByEmployeeId(String employeeId) {
    DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE_ALLOWANCE.EMPLOYEE_ALLOWANCE_ID,
            POLICY_TYPE.POLICY_TYPE_.as("policy_type"),
            POLICY_NAME.POLICY_NAME_.as("allowance_name"))
        .from(EMPLOYEE_ALLOWANCE)
        .leftJoin(POLICY_NAME)
        .on(POLICY_NAME.POLICY_NAME_ID.eq(EMPLOYEE_ALLOWANCE.POLICY_NAME_ID))
        .leftJoin(POLICY_TYPE)
        .on(POLICY_TYPE.POLICY_TYPE_ID.eq(POLICY_NAME.POLICY_TYPE_ID))
        .where(EMPLOYEE_ALLOWANCE.EMPLOYEE_ID.eq(employeeId))
        .and(EMPLOYEE_ALLOWANCE.ALLOWANCE_STATUS.isTrue())
        .fetchInto(EmployeeAllowanceResponse.class);
  }
}
