package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.EDeduction;
import com.csproject.hrm.dto.dto.DeductionTypeDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyInfoDto;
import com.csproject.hrm.dto.request.DeductionSalaryRequest;
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
import java.util.Optional;

import static org.jooq.codegen.maven.example.tables.DeductionSalary.DEDUCTION_SALARY;
import static org.jooq.codegen.maven.example.tables.DeductionType.DEDUCTION_TYPE;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.SalaryContract.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
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
  public List<DeductionSalaryResponse> getListDeductionMonthlyBySalaryMonthlyId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<DeductionSalaryResponse> deductionSalaryResponses =
        dslContext
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
            .fetchInto(DeductionSalaryResponse.class);
    return deductionSalaryResponses;
  }

  @Override
  public BigDecimal sumListDeductionMonthlyBySalaryMonthlyId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(sum(DEDUCTION_SALARY.VALUE))
        .from(DEDUCTION_SALARY)
        .leftJoin(DEDUCTION_TYPE)
        .on(DEDUCTION_TYPE.DEDUCTION_TYPE_ID.eq(DEDUCTION_SALARY.DEDUCTION_TYPE_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(DEDUCTION_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .fetchOneInto(BigDecimal.class);
  }

  @Override
  public void updateDeductionSalaryByDeductionSalaryId(DeductionSalaryRequest deductionSalaryRequest) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(DEDUCTION_SALARY)
            .set(DEDUCTION_SALARY.VALUE, deductionSalaryRequest.getValue())
            .set(DEDUCTION_SALARY.DESCRIPTION, deductionSalaryRequest.getDescription())
            .set(DEDUCTION_SALARY.DATE, deductionSalaryRequest.getDate())
            .set(DEDUCTION_SALARY.DEDUCTION_TYPE_ID, deductionSalaryRequest.getDeductionTypeId())
            .where(DEDUCTION_SALARY.DEDUCTION_ID.eq(deductionSalaryRequest.getDeductionSalaryId()))
            .execute();
  }

  @Override
  public void deleteDeductionSalaryByDeductionSalaryId(Long deductionSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .delete(DEDUCTION_SALARY)
            .where(DEDUCTION_SALARY.DEDUCTION_ID.eq(deductionSalaryId))
            .execute();
  }

  @Override
  public Optional<SalaryMonthlyInfoDto> getSalaryMonthlyInfoByDeductionSalary(
      Long deductionSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    System.out.println(
        dslContext
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
            .leftJoin(DEDUCTION_SALARY)
            .on(DEDUCTION_SALARY.SALARY_ID.eq(SALARY_MONTHLY.SALARY_ID))
            .where(DEDUCTION_SALARY.DEDUCTION_ID.eq(deductionSalaryId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue()));
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
        .leftJoin(DEDUCTION_SALARY)
        .on(DEDUCTION_SALARY.SALARY_ID.eq(SALARY_MONTHLY.SALARY_ID))
        .where(DEDUCTION_SALARY.DEDUCTION_ID.eq(deductionSalaryId))
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
        .fetchOptionalInto(SalaryMonthlyInfoDto.class);
  }

  @Override
  public boolean checkExistDeductionSalary(Long deductionSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select()
            .from(DEDUCTION_SALARY)
            .where(DEDUCTION_SALARY.DEDUCTION_ID.eq(deductionSalaryId)));
  }

  @Override
  public List<DeductionTypeDto> getListDeductionTypeDto() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<DeductionTypeDto> deductionTypeDtos =
        dslContext
            .select(
                DEDUCTION_TYPE.DEDUCTION_TYPE_ID,
                DEDUCTION_TYPE.DEDUCTION_TYPE_.as("deduction_type_name"))
            .from(DEDUCTION_TYPE)
            .fetchInto(DeductionTypeDto.class);
    deductionTypeDtos.forEach(
        deductionTypeDto -> {
          deductionTypeDto.setDeduction_type_name(
              EDeduction.getLabel(deductionTypeDto.getDeduction_type_name()));
        });
    return deductionTypeDtos;
  }
}
