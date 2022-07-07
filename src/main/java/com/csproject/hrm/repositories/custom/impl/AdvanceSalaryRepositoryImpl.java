package com.csproject.hrm.repositories.custom.impl;

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
import java.util.Map;

import static org.jooq.codegen.maven.example.tables.AdvancesSalary.ADVANCES_SALARY;

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
}
