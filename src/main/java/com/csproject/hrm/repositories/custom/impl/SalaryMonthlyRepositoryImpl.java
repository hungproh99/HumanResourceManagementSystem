package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.custom.SalaryMonthlyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
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
  public List<SalaryMonthlyDetailResponse> getAllSalaryMonthly(QueryParam queryParam, String managerId) {
    return null;
  }

  @Override
  public Long getSalaryMonthlyIdByEmployeeIdAndDate(
      String employeeId, LocalDate startDate, LocalDate endDate) {
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
    boolean checkExist =
        dslContext.fetchExists(
            dslContext
                .select(SALARY_MONTHLY.SALARY_ID)
                .from(SALARY_MONTHLY)
                .where(SALARY_MONTHLY.SALARY_CONTRACT_ID.eq(salaryContractId))
                .and(SALARY_MONTHLY.START_DATE.le(startDate))
                .and(SALARY_MONTHLY.END_DATE.ge(endDate)));
    SalaryMonthlyResponse salaryMonthlyResponse = new SalaryMonthlyResponse();
    salaryMonthlyResponse.setEmployeeId(employeeId);
    salaryMonthlyResponse.setStartDate(startDate);
    salaryMonthlyResponse.setEndDate(endDate);
    if (!checkExist) {
      insertSalaryMonthlyByEmployee(salaryMonthlyResponse).execute();
    }
    return dslContext
        .select(SALARY_MONTHLY.SALARY_ID)
        .from(SALARY_MONTHLY)
        .where(SALARY_MONTHLY.SALARY_CONTRACT_ID.eq(salaryContractId))
        .and(SALARY_MONTHLY.START_DATE.le(startDate))
        .and(SALARY_MONTHLY.END_DATE.ge(endDate))
        .fetchOneInto(Long.class);
  }

  @Override
  public void insertSalaryMonthlyByListEmployee(
      List<SalaryMonthlyResponse> salaryMonthlyResponseList) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var queries =
        salaryMonthlyResponseList.stream()
            .map(this::insertSalaryMonthlyByEmployee)
            .toArray(Query[]::new);
    dslContext.batch(queries).execute();
  }

  private InsertReturningStep<?> insertSalaryMonthlyByEmployee(
      SalaryMonthlyResponse salaryMonthlyResponse) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> contractTable =
        dslContext
            .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(WORKING_CONTRACT)
            .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(salaryMonthlyResponse.getEmployeeId()))
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
        .insertInto(
            SALARY_MONTHLY,
            SALARY_MONTHLY.END_DATE,
            SALARY_MONTHLY.FINAL_SALARY,
            SALARY_MONTHLY.START_DATE,
            SALARY_MONTHLY.SALARY_CONTRACT_ID,
            SALARY_MONTHLY.ACTUAL_POINT,
            SALARY_MONTHLY.OT_POINT,
            SALARY_MONTHLY.STANDARD_POINT,
            SALARY_MONTHLY.TOTAL_ADVANCE,
            SALARY_MONTHLY.TOTAL_BONUS,
            SALARY_MONTHLY.TOTAL_DEDUCTION,
            SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT,
            SALARY_MONTHLY.TOTAL_TAX_PAYMENT)
        .values(
            salaryMonthlyResponse.getEndDate(),
            salaryMonthlyResponse.getFinalSalary(),
            salaryMonthlyResponse.getStartDate(),
            salaryContractId,
            salaryMonthlyResponse.getActualPoint(),
            salaryMonthlyResponse.getOtPoint(),
            salaryMonthlyResponse.getStandardPoint(),
            salaryMonthlyResponse.getTotalAdvance(),
            salaryMonthlyResponse.getTotalBonus(),
            salaryMonthlyResponse.getTotalDeduction(),
            salaryMonthlyResponse.getTotalInsurance(),
            salaryMonthlyResponse.getTotalTax())
        .onConflictDoNothing();
  }
}