package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.AdvanceSalaryDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyInfoDto;
import com.csproject.hrm.dto.response.AdvanceSalaryResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.AdvanceSalaryRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.jooq.codegen.maven.example.tables.AdvancesSalary.ADVANCES_SALARY;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.SalaryContract.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.sum;

@AllArgsConstructor
public class AdvanceSalaryRepositoryImpl implements AdvanceSalaryRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertAdvanceSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, BigDecimal bonus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .insertInto(
                ADVANCES_SALARY,
                ADVANCES_SALARY.DATE,
                ADVANCES_SALARY.DESCRIPTION,
                ADVANCES_SALARY.VALUE,
                ADVANCES_SALARY.SALARY_ID)
            .values(date, description, bonus, salaryId)
            .execute();
  }

  @Override
  public List<AdvanceSalaryResponse> getListAdvanceMonthlyBySalaryMonthlyId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            ADVANCES_SALARY.ADVANCES_ID,
            ADVANCES_SALARY.VALUE,
            ADVANCES_SALARY.DATE,
            ADVANCES_SALARY.DESCRIPTION)
        .from(ADVANCES_SALARY)
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(ADVANCES_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .fetchInto(AdvanceSalaryResponse.class);
  }

  @Override
  public BigDecimal sumListAdvanceMonthlyBySalaryMonthlyId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(sum(ADVANCES_SALARY.VALUE))
        .from(ADVANCES_SALARY)
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(ADVANCES_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .fetchOneInto(BigDecimal.class);
  }

  @Override
  public void updateAdvanceSalaryByAdvanceId(AdvanceSalaryDto advanceSalaryDto) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(ADVANCES_SALARY)
            .set(ADVANCES_SALARY.VALUE, advanceSalaryDto.getValue())
            .set(ADVANCES_SALARY.DESCRIPTION, advanceSalaryDto.getDescription())
            .set(ADVANCES_SALARY.DATE, advanceSalaryDto.getDate())
            .where(ADVANCES_SALARY.ADVANCES_ID.eq(advanceSalaryDto.getAdvanceId()))
            .execute();
  }

  @Override
  public void deleteAdvanceSalaryByAdvanceId(Long advanceId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .delete(ADVANCES_SALARY)
            .where(ADVANCES_SALARY.ADVANCES_ID.eq(advanceId))
            .execute();
  }

  @Override
  public Optional<SalaryMonthlyInfoDto> getSalaryMonthlyInfoByAdvanceSalary(Long advanceSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
            SALARY_MONTHLY.START_DATE.as("startDate"),
            SALARY_MONTHLY.END_DATE.as("endDate"))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(SALARY_CONTRACT)
        .on(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_CONTRACT_ID.eq(SALARY_CONTRACT.SALARY_CONTRACT_ID))
        .leftJoin(ADVANCES_SALARY)
        .on(ADVANCES_SALARY.SALARY_ID.eq(SALARY_MONTHLY.SALARY_ID))
        .where(ADVANCES_SALARY.ADVANCES_ID.eq(advanceSalaryId))
        .fetchOptionalInto(SalaryMonthlyInfoDto.class);
  }

  @Override
  public boolean checkExistAdvanceSalary(Long advanceSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select()
            .from(ADVANCES_SALARY)
            .where(ADVANCES_SALARY.ADVANCES_ID.eq(advanceSalaryId)));
  }
}
