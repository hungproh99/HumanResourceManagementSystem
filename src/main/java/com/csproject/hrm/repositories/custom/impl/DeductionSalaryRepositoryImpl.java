package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.DeductionSalaryResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.DeductionSalaryRepositoryCustom;
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

import static org.jooq.codegen.maven.example.tables.DeductionSalary.DEDUCTION_SALARY;
import static org.jooq.codegen.maven.example.tables.DeductionType.DEDUCTION_TYPE;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.impl.DSL.sum;

@AllArgsConstructor
public class DeductionSalaryRepositoryImpl implements DeductionSalaryRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertDeductionSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long deductionType, BigDecimal bonus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .insertInto(
                DEDUCTION_SALARY,
                DEDUCTION_SALARY.DATE,
                DEDUCTION_SALARY.DESCRIPTION,
                DEDUCTION_SALARY.VALUE,
                DEDUCTION_SALARY.SALARY_ID,
                DEDUCTION_SALARY.DEDUCTION_TYPE_ID)
            .values(date, description, bonus, salaryId, deductionType)
            .execute();
  }

  @Override
  public List<DeductionSalaryResponse> getListDeductionBySalaryIdAndMonth(
      Long salaryId, LocalDate startDate, LocalDate endDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            DEDUCTION_SALARY.DEDUCTION_ID,
            DEDUCTION_SALARY.VALUE,
            DEDUCTION_TYPE.DEDUCTION_TYPE_.as("deduction_name"),
            DEDUCTION_SALARY.DATE,
            DEDUCTION_SALARY.DESCRIPTION)
        .from(DEDUCTION_SALARY)
        .leftJoin(DEDUCTION_TYPE)
        .on(DEDUCTION_TYPE.DEDUCTION_TYPE_ID.eq(DEDUCTION_SALARY.DEDUCTION_TYPE_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(DEDUCTION_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .and(SALARY_MONTHLY.START_DATE.eq(startDate))
        .and(SALARY_MONTHLY.END_DATE.eq(endDate))
        .fetchInto(DeductionSalaryResponse.class);
  }

  @Override
  public BigDecimal sumDeductionBySalaryIdAndMonth(
      Long salaryId, LocalDate startDate, LocalDate endDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(sum(DEDUCTION_SALARY.VALUE))
        .from(DEDUCTION_SALARY)
        .leftJoin(DEDUCTION_TYPE)
        .on(DEDUCTION_TYPE.DEDUCTION_TYPE_ID.eq(DEDUCTION_SALARY.DEDUCTION_TYPE_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(DEDUCTION_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .and(SALARY_MONTHLY.START_DATE.eq(startDate))
        .and(SALARY_MONTHLY.END_DATE.eq(endDate))
        .fetchOneInto(BigDecimal.class);
  }
}