package com.csproject.hrm.repositories.custom.impl;

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
import java.util.Map;

import static org.jooq.codegen.maven.example.tables.DeductionSalary.DEDUCTION_SALARY;

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
}
