package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.EmployeeTaxResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.EmployeeTaxRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.codegen.maven.example.Tables.POLICY_NAME;
import static org.jooq.codegen.maven.example.tables.EmployeeTax.EMPLOYEE_TAX;
import static org.jooq.codegen.maven.example.tables.PolicyType.POLICY_TYPE;

@AllArgsConstructor
public class EmployeeTaxRepositoryImpl implements EmployeeTaxRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<EmployeeTaxResponse> getListTaxByEmployeeId(String employeeId) {
    DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE_TAX.EMPLOYEE_TAX_ID,
            POLICY_TYPE.POLICY_TYPE_.as("policy_type"),
            POLICY_NAME.POLICY_NAME_.as("tax_name"))
        .from(EMPLOYEE_TAX)
        .leftJoin(POLICY_NAME)
        .on(POLICY_NAME.POLICY_NAME_ID.eq(EMPLOYEE_TAX.POLICY_NAME_ID))
        .leftJoin(POLICY_TYPE)
        .on(POLICY_TYPE.POLICY_TYPE_ID.eq(POLICY_NAME.POLICY_TYPE_ID))
        .where(EMPLOYEE_TAX.EMPLOYEE_ID.eq(employeeId))
        .and(EMPLOYEE_TAX.TAX_STATUS.isTrue())
        .fetchInto(EmployeeTaxResponse.class);
  }
}
