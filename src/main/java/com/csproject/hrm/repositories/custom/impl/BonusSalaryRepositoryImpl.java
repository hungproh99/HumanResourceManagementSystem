package com.csproject.hrm.repositories.custom.impl;

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
import java.util.Map;

import static org.jooq.codegen.maven.example.tables.BonusSalary.BONUS_SALARY;

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
      Long salaryId, LocalDate date, String description, BigDecimal bonus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .insertInto(
                BONUS_SALARY,
                BONUS_SALARY.DATE,
                BONUS_SALARY.DESCRIPTION,
                BONUS_SALARY.VALUE,
                BONUS_SALARY.SALARY_ID)
            .values(date, description, bonus, salaryId)
            .execute();
  }
}