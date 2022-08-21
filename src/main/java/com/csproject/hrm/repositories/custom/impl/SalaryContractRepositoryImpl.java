package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.SalaryContractDto;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.SalaryContractRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.year;

@AllArgsConstructor
public class SalaryContractRepositoryImpl implements SalaryContractRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertNewSalaryContractByIncreaseSalary(
      String employeeId, BigDecimal newSalary, LocalDate startDate, boolean status) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var workingContractId =
        dslContext
            .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .fetchOneInto(Long.class);

    final var oldBaseSalary =
        dslContext
            .select(SALARY_CONTRACT.BASE_SALARY)
            .from(SALARY_CONTRACT)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .fetchOneInto(BigDecimal.class);

    final var insertNewSalaryContract =
        dslContext
            .insertInto(
                SALARY_CONTRACT,
                SALARY_CONTRACT.ADDITIONAL_SALARY,
                SALARY_CONTRACT.SALARY_CONTRACT_STATUS,
                SALARY_CONTRACT.WORKING_CONTRACT_ID,
                SALARY_CONTRACT.START_DATE)
            .values(newSalary.subtract(oldBaseSalary), status, workingContractId, startDate)
            .execute();
  }

  @Override
  public void insertSalaryContract(String employeeId, BigDecimal baseSalary, BigDecimal finalSalary,
                                   LocalDate startDate, boolean status, Long workingContractId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    final var oldBaseSalary =
        dslContext
            .select(SALARY_CONTRACT.BASE_SALARY)
            .from(SALARY_CONTRACT)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .fetchOneInto(BigDecimal.class);

    dslContext
        .insertInto(
            SALARY_CONTRACT,
            SALARY_CONTRACT.ADDITIONAL_SALARY,
            SALARY_CONTRACT.BASE_SALARY,
            SALARY_CONTRACT.SALARY_CONTRACT_STATUS,
            SALARY_CONTRACT.WORKING_CONTRACT_ID,
            SALARY_CONTRACT.START_DATE)
        .values(
            finalSalary.subtract(oldBaseSalary), baseSalary, status, workingContractId, startDate)
        .execute();
  }

  @Override
  public void updateSalaryContract(
      String employeeId, BigDecimal newSalary, LocalDate startDate, boolean status) {
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

    dslContext
        .update(SALARY_CONTRACT)
        .set(SALARY_CONTRACT.BASE_SALARY, newSalary)
        .set(SALARY_CONTRACT.START_DATE, startDate)
        .set(SALARY_CONTRACT.SALARY_CONTRACT_STATUS, status)
        .where(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(contractId))
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
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

  @Override
  public void updateStatusSalaryContract(Boolean status, LocalDate dateCheck) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Long> salaryContractIdList =
        dslContext
            .select(SALARY_CONTRACT.SALARY_CONTRACT_ID)
            .from(SALARY_CONTRACT)
            .where(month(SALARY_CONTRACT.START_DATE).eq(dateCheck.getMonth().getValue()))
            .and(year(SALARY_CONTRACT.START_DATE).eq(dateCheck.getYear()))
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isFalse())
            .fetchInto(Long.class);

    List<Query> queries = new ArrayList<>();
    dslContext.transaction(
        configuration -> {
          salaryContractIdList.forEach(
              salaryContractId -> {
                Long workingContractId =
                    dslContext
                        .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
                        .from(WORKING_CONTRACT)
                        .leftJoin(SALARY_CONTRACT)
                        .on(
                            SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(
                                WORKING_CONTRACT.WORKING_CONTRACT_ID))
                        .where(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(salaryContractId))
                        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
                        .fetchOneInto(Long.class);

                dslContext
                    .update(SALARY_CONTRACT)
                    .set(SALARY_CONTRACT.SALARY_CONTRACT_STATUS, false)
                    .where(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(workingContractId))
                    .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
                    .execute();

                queries.add(
                    dslContext
                        .update(SALARY_CONTRACT)
                        .set(SALARY_CONTRACT.SALARY_CONTRACT_STATUS, status)
                        .where(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(salaryContractId)));
              });
        });
    dslContext.batch(queries).execute();
  }
}