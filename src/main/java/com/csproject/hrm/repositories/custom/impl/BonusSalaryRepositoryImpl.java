package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.BonusSalaryResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.BonusSalaryRepositoryCustom;
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

import static org.jooq.codegen.maven.example.tables.BonusSalary.BONUS_SALARY;
import static org.jooq.codegen.maven.example.tables.BonusType.BONUS_TYPE;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.impl.DSL.sum;

@AllArgsConstructor
public class BonusSalaryRepositoryImpl implements BonusSalaryRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertBonusSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long bonusType, BigDecimal bonus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .insertInto(
                BONUS_SALARY,
                BONUS_SALARY.DATE,
                BONUS_SALARY.DESCRIPTION,
                BONUS_SALARY.VALUE,
                BONUS_SALARY.SALARY_ID,
                BONUS_SALARY.BONUS_TYPE_ID)
            .values(date, description, bonus, salaryId, bonusType)
            .execute();
  }

  @Override
  public List<BonusSalaryResponse> getListBonusBySalaryIdAndMonth(
      Long salaryId, LocalDate startDate, LocalDate endDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            BONUS_SALARY.BONUS_ID,
            BONUS_SALARY.VALUE,
            BONUS_TYPE.BONUS_TYPE_.as("bonus_name"),
            BONUS_SALARY.DATE,
            BONUS_SALARY.DESCRIPTION)
        .from(BONUS_SALARY)
        .leftJoin(BONUS_TYPE)
        .on(BONUS_TYPE.BONUS_TYPE_ID.eq(BONUS_SALARY.BONUS_TYPE_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(BONUS_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .and(SALARY_MONTHLY.START_DATE.eq(startDate))
        .and(SALARY_MONTHLY.END_DATE.eq(endDate))
        .fetchInto(BonusSalaryResponse.class);
  }

  @Override
  public BigDecimal sumBonusBySalaryIdAndMonth(
      Long salaryId, LocalDate startDate, LocalDate endDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(sum(BONUS_SALARY.VALUE))
        .from(BONUS_SALARY)
        .leftJoin(BONUS_TYPE)
        .on(BONUS_TYPE.BONUS_TYPE_ID.eq(BONUS_SALARY.BONUS_TYPE_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(BONUS_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .and(SALARY_MONTHLY.START_DATE.eq(startDate))
        .and(SALARY_MONTHLY.END_DATE.eq(endDate))
        .fetchOneInto(BigDecimal.class);
  }
}