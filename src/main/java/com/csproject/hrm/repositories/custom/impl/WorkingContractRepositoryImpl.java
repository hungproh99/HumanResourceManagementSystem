package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.repositories.custom.WorkingContractRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.jooq.codegen.maven.example.Tables.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;

@AllArgsConstructor
public class WorkingContractRepositoryImpl implements WorkingContractRepositoryCustom {

  @Autowired private final DBConnection connection;

  @Override
  public BigDecimal getBaseSalaryByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(SALARY_CONTRACT.BASE_SALARY)
        .from(WORKING_CONTRACT)
        .leftJoin(SALARY_CONTRACT)
        .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
        .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeID))
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
        .fetchOneInto(BigDecimal.class);
  }
}