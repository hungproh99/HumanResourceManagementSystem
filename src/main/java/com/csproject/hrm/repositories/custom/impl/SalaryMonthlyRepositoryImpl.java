package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.ESalaryMonthly;
import com.csproject.hrm.dto.dto.SalaryMonthlyDto;
import com.csproject.hrm.dto.dto.UpdateStatusSalaryMonthlyDto;
import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.SalaryMonthlyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.codegen.maven.example.Tables;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.SalaryContract.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.noCondition;

@AllArgsConstructor
public class SalaryMonthlyRepositoryImpl implements SalaryMonthlyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
    field2Map.put(START_DATE, SALARY_MONTHLY.START_DATE);
    field2Map.put(END_DATE, SALARY_MONTHLY.END_DATE);
    field2Map.put(SALARY_STATUS_PARAM, SALARY_STATUS.NAME);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public SalaryMonthlyResponseList getAllSalaryMonthly(
      QueryParam queryParam, List<String> employeeIdList, String role) {
    List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));
    LocalDate currDate = LocalDate.now();
    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(END_DATE)) {
              condition = condition.and(queryHelper.condition(filter, field));
              LocalDate endDate = LocalDate.parse(filter.condition.substring(3));
              if (endDate.isAfter(currDate)) {
                throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST, "Can't get salary monthly until end of month");
              }
            } else {
              condition = condition.and(queryHelper.condition(filter, field));
            }
          }
          conditions.add(condition);
        });

    Condition condition = noCondition();
    for (String employeeId : employeeIdList) {
      condition = condition.or(EMPLOYEE.EMPLOYEE_ID.eq(employeeId));
    }
    if (role.equalsIgnoreCase("USER")) {
      condition = condition.and(SALARY_STATUS.NAME.eq(ESalaryMonthly.APPROVED.name()));
    }
    conditions.add(condition);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    System.out.println(getAllSalaryMonthly(conditions, sortFields, queryParam.pagination));
    List<SalaryMonthlyResponse> salaryMonthlyResponses =
        getAllSalaryMonthly(conditions, sortFields, queryParam.pagination)
            .fetchInto(SalaryMonthlyResponse.class);
    int total = countAllSalaryMonthly(conditions);
    return new SalaryMonthlyResponseList(salaryMonthlyResponses, total);
  }

  @Override
  public Optional<SalaryMonthlyResponse> getSalaryMonthlyBySalaryId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            SALARY_MONTHLY.SALARY_ID.as("salaryMonthlyId"),
            EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
            EMPLOYEE.FULL_NAME.as("fullName"),
            JOB.POSITION.as("position"),
            SALARY_MONTHLY.STANDARD_POINT.as("standardPoint"),
            SALARY_MONTHLY.ACTUAL_POINT.as("actualPoint"),
            SALARY_MONTHLY.OT_POINT.as("otPoint"),
            SALARY_MONTHLY.TOTAL_DEDUCTION.as("totalDeduction"),
            SALARY_MONTHLY.TOTAL_BONUS.as("totalBonus"),
            SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT.as("totalInsurance"),
            SALARY_MONTHLY.TOTAL_TAX_PAYMENT.as("totalTax"),
            SALARY_MONTHLY.TOTAL_ADVANCE.as("totalAdvance"),
            SALARY_MONTHLY.FINAL_SALARY.as("finalSalary"),
            SALARY_MONTHLY.START_DATE.as("startDate"),
            SALARY_MONTHLY.END_DATE.as("endDate"),
            SALARY_STATUS.NAME.as("salaryStatus"))
        .from(SALARY_MONTHLY)
        .leftJoin(SALARY_CONTRACT)
        .on(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(SALARY_MONTHLY.SALARY_CONTRACT_ID))
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(Tables.WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(SALARY_STATUS)
        .on(SALARY_STATUS.STATUS_ID.eq(SALARY_MONTHLY.SALARY_STATUS_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .fetchOptionalInto(SalaryMonthlyResponse.class);
  }

  @Override
  public Long getSalaryMonthlyIdByEmployeeIdAndDate(
      String employeeId, LocalDate startDate, LocalDate endDate, String salaryStatus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    Long salaryContractId =
        dslContext
            .select(SALARY_CONTRACT.SALARY_CONTRACT_ID)
            .from(SALARY_CONTRACT)
            .leftJoin(WORKING_CONTRACT)
            .on(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .where(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeId))
            .fetchOneInto(Long.class);
    boolean checkExist = checkExistSalaryMonthly(startDate, endDate, salaryContractId);
    if (!checkExist) {
      insertSalaryMonthlyByEmployee(salaryContractId, startDate, endDate, salaryStatus).execute();
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
  public void updateSalaryMonthlyByListEmployee(List<SalaryMonthlyDto> salaryMonthlyDtoList) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var queries =
        salaryMonthlyDtoList.stream()
            .map(this::updateSalaryMonthlyByEmployee)
            .toArray(Query[]::new);
    dslContext.batch(queries).execute();
  }

  @Override
  public List<SalaryMonthlyResponse> getListSalaryMonthlyToExport(
      QueryParam queryParam, List<Long> list) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    Condition condition = DSL.noCondition();
    for (Long salaryMonthId : list) {
      condition.or(SALARY_MONTHLY.SALARY_ID.eq(salaryMonthId));
    }
    conditions.add(condition);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    return getAllSalaryMonthly(conditions, sortFields, queryParam.pagination)
        .fetchInto(SalaryMonthlyResponse.class);
  }

  @Override
  public void updateStatusSalaryMonthlyBySalaryMonthlyId(
      UpdateStatusSalaryMonthlyDto updateStatusSalaryMonthlyDto) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(SALARY_MONTHLY)
            .set(SALARY_MONTHLY.SALARY_STATUS_ID, updateStatusSalaryMonthlyDto.getStatusSalary())
            .where(SALARY_MONTHLY.SALARY_ID.eq(updateStatusSalaryMonthlyDto.getSalaryMonthlyId()))
            .execute();
  }

  @Override
  public boolean checkExistSalaryMonthly(Long salaryMonthlyId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select()
            .from(SALARY_MONTHLY)
            .where(SALARY_MONTHLY.SALARY_ID.eq(salaryMonthlyId)));
  }

  private InsertReturningStep<?> insertSalaryMonthlyByEmployee(
      Long salaryContractId, LocalDate startDate, LocalDate endDate, String salaryStatus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .insertInto(
            SALARY_MONTHLY,
            SALARY_MONTHLY.END_DATE,
            SALARY_MONTHLY.START_DATE,
            SALARY_MONTHLY.SALARY_CONTRACT_ID,
            SALARY_MONTHLY.SALARY_STATUS_ID)
        .values(endDate, startDate, salaryContractId, ESalaryMonthly.getValue(salaryStatus))
        .onConflictDoNothing();
  }

  private UpdateReturningStep<?> updateSalaryMonthlyByEmployee(SalaryMonthlyDto salaryMonthlyDto) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .update(SALARY_MONTHLY)
        .set(SALARY_MONTHLY.FINAL_SALARY, salaryMonthlyDto.getFinalSalary())
        .set(SALARY_MONTHLY.ACTUAL_POINT, salaryMonthlyDto.getActualPoint())
        .set(SALARY_MONTHLY.OT_POINT, salaryMonthlyDto.getOtPoint())
        .set(SALARY_MONTHLY.STANDARD_POINT, salaryMonthlyDto.getStandardPoint())
        .set(SALARY_MONTHLY.TOTAL_ADVANCE, salaryMonthlyDto.getTotalAdvance())
        .set(SALARY_MONTHLY.TOTAL_BONUS, salaryMonthlyDto.getTotalBonus())
        .set(SALARY_MONTHLY.TOTAL_DEDUCTION, salaryMonthlyDto.getTotalDeduction())
        .set(SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT, salaryMonthlyDto.getTotalInsurance())
        .set(SALARY_MONTHLY.TOTAL_TAX_PAYMENT, salaryMonthlyDto.getTotalTax())
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryMonthlyDto.getSalaryMonthlyId()));
  }

  private boolean checkExistSalaryMonthly(
      LocalDate startDate, LocalDate endDate, Long salaryContractId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(SALARY_MONTHLY.SALARY_ID)
            .from(SALARY_MONTHLY)
            .where(SALARY_MONTHLY.SALARY_CONTRACT_ID.eq(salaryContractId))
            .and(SALARY_MONTHLY.START_DATE.le(startDate))
            .and(SALARY_MONTHLY.END_DATE.ge(endDate)));
  }

  private int countAllSalaryMonthly(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select()
            .from(SALARY_MONTHLY)
            .leftJoin(SALARY_CONTRACT)
            .on(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(SALARY_MONTHLY.SALARY_CONTRACT_ID))
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(Tables.WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(JOB)
            .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
            .leftJoin(SALARY_STATUS)
            .on(SALARY_STATUS.STATUS_ID.eq(SALARY_MONTHLY.SALARY_STATUS_ID))
            .where(conditions));
  }

  private Select<?> getAllSalaryMonthly(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            SALARY_MONTHLY.SALARY_ID.as("salaryMonthlyId"),
            EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
            EMPLOYEE.FULL_NAME.as("fullName"),
            JOB.POSITION.as("position"),
            SALARY_MONTHLY.STANDARD_POINT.as("standardPoint"),
            SALARY_MONTHLY.ACTUAL_POINT.as("actualPoint"),
            SALARY_MONTHLY.OT_POINT.as("otPoint"),
            SALARY_MONTHLY.TOTAL_DEDUCTION.as("totalDeduction"),
            SALARY_MONTHLY.TOTAL_BONUS.as("totalBonus"),
            SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT.as("totalInsurance"),
            SALARY_MONTHLY.TOTAL_TAX_PAYMENT.as("totalTax"),
            SALARY_MONTHLY.TOTAL_ADVANCE.as("totalAdvance"),
            SALARY_MONTHLY.FINAL_SALARY.as("finalSalary"),
            SALARY_MONTHLY.START_DATE.as("startDate"),
            SALARY_MONTHLY.END_DATE.as("endDate"),
            SALARY_STATUS.NAME.as("salaryStatus"))
        .from(SALARY_MONTHLY)
        .leftJoin(SALARY_CONTRACT)
        .on(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(SALARY_MONTHLY.SALARY_CONTRACT_ID))
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(Tables.WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(SALARY_STATUS)
        .on(SALARY_STATUS.STATUS_ID.eq(SALARY_MONTHLY.SALARY_STATUS_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }
}
