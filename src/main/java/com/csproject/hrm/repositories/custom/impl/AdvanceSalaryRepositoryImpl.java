package com.csproject.hrm.repositories.custom.impl;

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

import static org.jooq.codegen.maven.example.tables.AdvancesSalary.ADVANCES_SALARY;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
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
  public List<AdvanceSalaryResponse> getListAdvanceBySalaryIdAndMonth(
      Long salaryId, LocalDate startDate, LocalDate endDate) {
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
        .and(SALARY_MONTHLY.START_DATE.eq(startDate))
        .and(SALARY_MONTHLY.END_DATE.eq(endDate))
        .fetchInto(AdvanceSalaryResponse.class);
  }

  @Override
  public BigDecimal sumAdvanceBySalaryIdAndMonth(
      Long salaryId, LocalDate startDate, LocalDate endDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(sum(ADVANCES_SALARY.VALUE))
        .from(ADVANCES_SALARY)
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(ADVANCES_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .and(SALARY_MONTHLY.START_DATE.eq(startDate))
        .and(SALARY_MONTHLY.END_DATE.eq(endDate))
        .fetchOneInto(BigDecimal.class);
  }
}
