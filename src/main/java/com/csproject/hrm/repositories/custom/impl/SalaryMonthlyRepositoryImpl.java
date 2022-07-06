package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.custom.SalaryMonthlyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.codegen.maven.example.tables.SalaryContract.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;

@AllArgsConstructor
public class SalaryMonthlyRepositoryImpl implements SalaryMonthlyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<SalaryMonthlyResponse> getAllSalaryMonthly(QueryParam queryParam) {
    return null;
  }

  @Override
  public Long getSalaryIdByEmployeeIdAndDate(String employeeId, LocalDate date) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> contractTable =
        dslContext
            .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(WORKING_CONTRACT)
            .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue());
    Long salaryContractId =
        dslContext
            .select(SALARY_CONTRACT.SALARY_CONTRACT_ID)
            .from(SALARY_CONTRACT)
            .where(
                SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(
                    contractTable.field(WORKING_CONTRACT.WORKING_CONTRACT_ID)))
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .fetchOneInto(Long.class);
    return dslContext
        .select(SALARY_MONTHLY.SALARY_ID)
        .from(SALARY_MONTHLY)
        .where(SALARY_MONTHLY.SALARY_CONTRACT_ID.eq(salaryContractId))
        .and(SALARY_MONTHLY.START_DATE.lt(date))
        .and(SALARY_MONTHLY.END_DATE.gt(date))
        .fetchOneInto(Long.class);
  }
}
