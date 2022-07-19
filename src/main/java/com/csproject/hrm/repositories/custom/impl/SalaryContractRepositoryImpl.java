package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.SalaryContractDto;
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
import java.util.Optional;

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

  @Override
  public Optional<SalaryContractDto> getSalaryContractByEmployeeId(String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            SALARY_CONTRACT.SALARY_CONTRACT_ID,
            SALARY_CONTRACT.BASE_SALARY,
            SALARY_CONTRACT.ADDITIONAL_SALARY,
            WORKING_TYPE.NAME.as("working_type"))
        .from(SALARY_CONTRACT)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
        .leftJoin(WORKING_TYPE)
        .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
        .where(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
        .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
        .fetchOptionalInto(SalaryContractDto.class);
  }
}
