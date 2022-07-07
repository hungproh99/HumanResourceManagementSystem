package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.SalaryContractRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.jooq.codegen.maven.example.Tables.*;

@AllArgsConstructor
public class SalaryContractRepositoryImpl implements SalaryContractRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertNewSalaryContract(
      String employeeId,
      BigDecimal newSalary,
      LocalDate startDate,
      boolean oldStatus,
      boolean newStatus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var contractId =
        dslContext
            .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .fetchOneInto(Long.class);

    final var updateStatusOldSalaryContract =
        dslContext
            .update(SALARY_CONTRACT)
            .set(SALARY_CONTRACT.SALARY_CONTRACT_STATUS, oldStatus)
            .where(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(contractId))
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .execute();

    final var insertNewSalaryContract =
        dslContext
            .insertInto(
                SALARY_CONTRACT,
                SALARY_CONTRACT.BASE_SALARY,
                SALARY_CONTRACT.SALARY_CONTRACT_STATUS,
                SALARY_CONTRACT.WORKING_CONTRACT_ID,
                SALARY_CONTRACT.START_DATE)
            .values(newSalary, newStatus, contractId, startDate)
            .execute();
  }
}
