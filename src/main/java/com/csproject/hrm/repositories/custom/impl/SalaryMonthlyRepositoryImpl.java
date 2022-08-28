package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.ESalaryMonthly;
import com.csproject.hrm.dto.dto.SalaryMonthlyDto;
import com.csproject.hrm.dto.request.RejectSalaryMonthlyRequest;
import com.csproject.hrm.dto.request.UpdateSalaryMonthlyRequest;
import com.csproject.hrm.dto.response.SalaryMonthlyRemindResponse;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.AREA;
import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.SalaryContract.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.when;

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
  public SalaryMonthlyResponseList getAllPersonalSalaryMonthly(
      QueryParam queryParam, String employeeId) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    Condition condition = noCondition();
    condition =
        condition
            .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(SALARY_STATUS.NAME.eq(ESalaryMonthly.APPROVED.name()));
    conditions.add(condition);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    System.out.println(getAllPersonalSalaryMonthly(conditions, sortFields, queryParam.pagination));
    List<SalaryMonthlyResponse> salaryMonthlyResponses =
        getAllPersonalSalaryMonthly(conditions, sortFields, queryParam.pagination)
            .fetchInto(SalaryMonthlyResponse.class);
    int total = countAllPersonalSalaryMonthly(conditions);
    return new SalaryMonthlyResponseList(salaryMonthlyResponses, total, "False");
  }

  @Override
  public SalaryMonthlyResponseList getAllManagementSalaryMonthly(
      QueryParam queryParam, String employeeId, String isEnoughLevel) {
    List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    final List<OrderField<?>> sortFields = new ArrayList<>();
    sortFields.add(
        when(
                SALARY_MONTHLY.SALARY_STATUS_ID.eq(
                    ESalaryMonthly.getValue(ESalaryMonthly.PENDING.name())),
                1)
            .when(
                SALARY_MONTHLY.SALARY_STATUS_ID.eq(
                    ESalaryMonthly.getValue(ESalaryMonthly.APPROVED.name())),
                2)
            .when(
                SALARY_MONTHLY.SALARY_STATUS_ID.eq(
                    ESalaryMonthly.getValue(ESalaryMonthly.REJECTED.name())),
                3)
            .asc());

    for (OrderByClause clause : queryParam.orderByList) {
      final Field<?> field = field2Map.get(clause.field);
      if (Objects.isNull(field)) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
      }
      if (clause.orderBy.equals(OrderBy.ASC)) {
        sortFields.add(field.asc().nullsLast());
      } else {
        sortFields.add(field.desc().nullsLast());
      }
    }

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
    System.out.println(
        getAllManagementSalaryMonthly(conditions, sortFields, queryParam.pagination, employeeId));
    List<SalaryMonthlyResponse> salaryMonthlyResponses =
        getAllManagementSalaryMonthly(conditions, sortFields, queryParam.pagination, employeeId)
            .fetchInto(SalaryMonthlyResponse.class);
    salaryMonthlyResponses.forEach(
        salaryMonthlyResponse -> {
          salaryMonthlyResponse.setChecked_by(
              getListForwarderBySalaryId(salaryMonthlyResponse.getSalaryMonthlyId())
                  .fetchInto(String.class));
        });

    int total = countAllManagementSalaryMonthly(conditions, employeeId);

    return new SalaryMonthlyResponseList(salaryMonthlyResponses, total, isEnoughLevel);
  }

  @Override
  public Optional<SalaryMonthlyResponse> getSalaryMonthlyBySalaryId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    Optional<SalaryMonthlyResponse> salaryMonthlyResponse =
        dslContext
            .select(
                SALARY_MONTHLY.SALARY_ID.as("salaryMonthlyId"),
                EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
                EMPLOYEE.FULL_NAME.as("fullName"),
                JOB.POSITION.as("position"),
                SALARY_MONTHLY.APPROVER.as("approverId"),
                SALARY_MONTHLY.STANDARD_POINT.as("standardPoint"),
                SALARY_MONTHLY.ACTUAL_POINT.as("actualPoint"),
                SALARY_MONTHLY.OT_POINT.as("otPoint"),
                SALARY_MONTHLY.TOTAL_DEDUCTION.as("totalDeduction"),
                SALARY_MONTHLY.TOTAL_BONUS.as("totalBonus"),
                SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT.as("totalInsurance"),
                SALARY_MONTHLY.TOTAL_TAX_PAYMENT.as("totalTax"),
                SALARY_MONTHLY.TOTAL_ADVANCE.as("totalAdvance"),
                SALARY_MONTHLY.TOTAL_ALLOWANCE.as("totalAllowance"),
                SALARY_MONTHLY.FINAL_SALARY.as("finalSalary"),
                SALARY_MONTHLY.START_DATE.as("startDate"),
                SALARY_MONTHLY.END_DATE.as("endDate"),
                SALARY_STATUS.NAME.as("salaryStatus"),
                SALARY_MONTHLY.COMMENT)
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
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
            .fetchOptionalInto(SalaryMonthlyResponse.class);
    salaryMonthlyResponse
        .get()
        .setChecked_by(
            getListForwarderBySalaryId(salaryMonthlyResponse.get().getSalaryMonthlyId())
                .fetchInto(String.class));
    return salaryMonthlyResponse;
  }

  @Override
  public Long getSalaryMonthlyIdByEmployeeIdAndDate(
      String employeeId,
      LocalDate startDate,
      LocalDate endDate,
      Double actualWorkingPoint,
      String salaryStatus) {
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
    String managerId =
        dslContext
            .select(EMPLOYEE.MANAGER_ID)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .fetchOneInto(String.class);
    String highestManagerId =
        dslContext
            .select(AREA.MANAGER_ID)
            .from(AREA)
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.AREA_ID.eq(AREA.AREA_ID))
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_PLACE.WORKING_CONTRACT_ID))
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
            .fetchOneInto(String.class);
    LocalDate duration = endDate.plusDays(3);
    if (managerId == null) {
      managerId = highestManagerId;
      duration = endDate.plusDays(7);
    }
    boolean checkExist = checkExistSalaryMonthly(startDate, endDate, salaryContractId);
    if (!checkExist) {
      insertSalaryMonthlyByEmployee(
              salaryContractId,
              startDate,
              endDate,
              salaryStatus,
              false,
              managerId,
              duration,
              actualWorkingPoint,
              0D,
              0D,
              BigDecimal.ZERO,
              BigDecimal.ZERO,
              BigDecimal.ZERO,
              BigDecimal.ZERO,
              BigDecimal.ZERO,
              BigDecimal.ZERO,
              BigDecimal.ZERO)
          .execute();
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
  public Long getSalaryMonthlyIdByEmployeeIdAndDate(
      String employeeId, LocalDate startDate, LocalDate endDate) {
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
  public void updateCheckedSalaryMonthly(
      UpdateSalaryMonthlyRequest updateSalaryMonthlyRequest, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          DSL.using(configuration)
              .update(SALARY_MONTHLY)
              .set(
                  SALARY_MONTHLY.SALARY_STATUS_ID,
                  ESalaryMonthly.getValue(updateSalaryMonthlyRequest.getSalaryStatus()))
              .set(SALARY_MONTHLY.APPROVER, updateSalaryMonthlyRequest.getApproverId())
              .where(SALARY_MONTHLY.SALARY_ID.eq(updateSalaryMonthlyRequest.getSalaryMonthlyId()))
              .execute();
          final var checkExistForwarder =
              dslContext.fetchExists(
                  dslContext
                      .select(REVIEW_SALARY.REVIEW_ID)
                      .from(REVIEW_SALARY)
                      .where(
                          REVIEW_SALARY.SALARY_ID.eq(
                              updateSalaryMonthlyRequest.getSalaryMonthlyId()))
                      .and(REVIEW_SALARY.EMPLOYEE_ID.eq(employeeId)));
          if (!checkExistForwarder) {
            final var insertForwarder =
                dslContext
                    .insertInto(REVIEW_SALARY, REVIEW_SALARY.EMPLOYEE_ID, REVIEW_SALARY.SALARY_ID)
                    .values(employeeId, updateSalaryMonthlyRequest.getSalaryMonthlyId())
                    .execute();
          }
        });
  }

  @Override
  public void updateRejectSalaryMonthly(RejectSalaryMonthlyRequest rejectSalaryMonthlyRequest) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var managerId =
        dslContext
            .select(EMPLOYEE.MANAGER_ID)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(SALARY_CONTRACT)
            .on(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(SALARY_MONTHLY)
            .on(SALARY_MONTHLY.SALARY_CONTRACT_ID.eq(SALARY_CONTRACT.SALARY_CONTRACT_ID))
            .where(SALARY_MONTHLY.SALARY_ID.eq(rejectSalaryMonthlyRequest.getSalaryMonthlyId()))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .fetchOneInto(String.class);

    final var query =
        dslContext
            .update(SALARY_MONTHLY)
            .set(
                SALARY_MONTHLY.SALARY_STATUS_ID,
                ESalaryMonthly.getValue(ESalaryMonthly.PENDING.name()))
            .set(SALARY_MONTHLY.COMMENT, rejectSalaryMonthlyRequest.getComment())
            .set(SALARY_MONTHLY.APPROVER, managerId)
            .where(SALARY_MONTHLY.SALARY_ID.eq(rejectSalaryMonthlyRequest.getSalaryMonthlyId()))
            .execute();
  }

  @Override
  public List<SalaryMonthlyResponse> getListPersonalSalaryMonthlyToExport(
      QueryParam queryParam, List<Long> list, String employeeId) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    conditions.add(
        DSL.noCondition()
            .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(SALARY_STATUS.NAME.eq(ESalaryMonthly.APPROVED.name())));
    Condition condition = DSL.noCondition();
    for (Long salaryMonthId : list) {
      condition = condition.or(SALARY_MONTHLY.SALARY_ID.eq(salaryMonthId));
    }
    conditions.add(condition);
    List<OrderField<?>> sortFields = new ArrayList<>();
    return getAllPersonalSalaryMonthly(conditions, sortFields, queryParam.pagination)
        .fetchInto(SalaryMonthlyResponse.class);
  }

  @Override
  public List<SalaryMonthlyResponse> getListManagementSalaryMonthlyToExport(
      QueryParam queryParam, List<Long> list, String employeeId) {
    List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));
    List<OrderField<?>> sortFields = new ArrayList<>();
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
    Condition condition = DSL.noCondition();
    for (Long salaryId : list) {
      condition = condition.or(SALARY_MONTHLY.SALARY_ID.eq(salaryId));
    }
    conditions.add(condition);
    List<SalaryMonthlyResponse> salaryMonthlyResponses =
        getAllManagementSalaryMonthly(conditions, sortFields, queryParam.pagination, employeeId)
            .fetchInto(SalaryMonthlyResponse.class);
    return salaryMonthlyResponses;
  }

  @Override
  public void updateStatusSalaryMonthlyBySalaryMonthlyId(
      Long salaryMonthlyId, String statusSalary) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(SALARY_MONTHLY)
            .set(SALARY_MONTHLY.SALARY_STATUS_ID, ESalaryMonthly.getValue(statusSalary))
            .where(SALARY_MONTHLY.SALARY_ID.eq(salaryMonthlyId))
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

  @Override
  public List<SalaryMonthlyRemindResponse> getAllSalaryMonthlyToRemind(LocalDate checkDate) {
    List<SalaryMonthlyRemindResponse> salaryMonthlyRemindResponseList =
        getListSalaryMonthlyRemind(checkDate).fetchInto(SalaryMonthlyRemindResponse.class);
    salaryMonthlyRemindResponseList.forEach(
        salaryMonthlyRemindResponse -> {
          salaryMonthlyRemindResponse.setCheckedBy(
              getListForwarderBySalaryMonthlyId(salaryMonthlyRemindResponse.getSalaryMonthlyId())
                  .fetchInto(String.class));
        });
    return salaryMonthlyRemindResponseList;
  }

  //  @Override
  //  public void updateAllSalaryMonthlyRemind(Long salaryMonthlyId, boolean isRemind) {
  //    final DSLContext dslContext = DSL.using(connection.getConnection());
  //    final var query =
  //        dslContext
  //            .update(SALARY_MONTHLY)
  //            .set(SALARY_MONTHLY.IS_REMIND, isRemind)
  //            .where(SALARY_MONTHLY.SALARY_ID.eq(salaryMonthlyId))
  //            .execute();
  //  }

  @Override
  public boolean checkAlreadyApproveOrReject(Long salaryMonthlyId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(SALARY_MONTHLY.SALARY_ID)
            .from(SALARY_MONTHLY)
            .where(SALARY_MONTHLY.SALARY_ID.eq(salaryMonthlyId))
            .and(
                SALARY_MONTHLY
                    .SALARY_STATUS_ID
                    .eq(ESalaryMonthly.getValue(ESalaryMonthly.REJECTED.name()))
                    .or(
                        SALARY_MONTHLY.SALARY_STATUS_ID.eq(
                            ESalaryMonthly.getValue(ESalaryMonthly.APPROVED.name())))));
  }

  @Override
  public void deleteAllReviewSalaryBySalaryId(Long salaryMonthId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext.delete(REVIEW_SALARY).where(REVIEW_SALARY.SALARY_ID.eq(salaryMonthId)).execute();
  }

  @Override
  public boolean checkExistSalaryMonthlyByDate(
      LocalDate startDate, LocalDate endDate, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(SALARY_MONTHLY.SALARY_ID)
            .from(SALARY_MONTHLY)
            .leftJoin(SALARY_CONTRACT)
            .on(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(SALARY_MONTHLY.SALARY_CONTRACT_ID))
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .where(SALARY_MONTHLY.START_DATE.eq(startDate))
            .and(SALARY_MONTHLY.END_DATE.eq(endDate))
            .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId)));
  }

  private Select<?> getListSalaryMonthlyRemind(LocalDate checkDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            SALARY_MONTHLY.SALARY_ID.as("salaryMonthlyId"),
            EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
            EMPLOYEE.FULL_NAME.as("fullName"),
            SALARY_MONTHLY.START_DATE.as("startDate"),
            SALARY_MONTHLY.START_DATE.as("endDate"),
            SALARY_MONTHLY.APPROVER.as("approver"),
            JOB.POSITION.as("position"),
            SALARY_MONTHLY.DURATION.as("duration"))
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
        .where(SALARY_MONTHLY.DURATION.le(checkDate))
        .and(
            SALARY_MONTHLY.SALARY_STATUS_ID.eq(
                ESalaryMonthly.getValue(ESalaryMonthly.PENDING.name())))
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue());
  }

  private Select<?> getListForwarderBySalaryMonthlyId(Long salaryMonthlyId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(REVIEW_SALARY.EMPLOYEE_ID)
        .from(REVIEW_SALARY)
        .where(REVIEW_SALARY.SALARY_ID.eq(salaryMonthlyId));
  }

  private InsertReturningStep<?> insertSalaryMonthlyByEmployee(
      Long salaryContractId,
      LocalDate startDate,
      LocalDate endDate,
      String salaryStatus,
      boolean isRemind,
      String approver,
      LocalDate duration,
      Double actualWorkingPoint,
      Double otPoint,
      Double standardPoint,
      BigDecimal finalSalary,
      BigDecimal totalAdvance,
      BigDecimal totalDeduction,
      BigDecimal totalBonus,
      BigDecimal totalInsurance,
      BigDecimal totalTax,
      BigDecimal totalAllowance) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .insertInto(
            SALARY_MONTHLY,
            SALARY_MONTHLY.END_DATE,
            SALARY_MONTHLY.START_DATE,
            SALARY_MONTHLY.SALARY_CONTRACT_ID,
            SALARY_MONTHLY.SALARY_STATUS_ID,
            SALARY_MONTHLY.IS_REMIND,
            SALARY_MONTHLY.APPROVER,
            SALARY_MONTHLY.DURATION,
            SALARY_MONTHLY.ACTUAL_POINT,
            SALARY_MONTHLY.OT_POINT,
            SALARY_MONTHLY.STANDARD_POINT,
            SALARY_MONTHLY.FINAL_SALARY,
            SALARY_MONTHLY.TOTAL_ADVANCE,
            SALARY_MONTHLY.TOTAL_DEDUCTION,
            SALARY_MONTHLY.TOTAL_BONUS,
            SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT,
            SALARY_MONTHLY.TOTAL_TAX_PAYMENT,
            SALARY_MONTHLY.TOTAL_ALLOWANCE)
        .values(
            endDate,
            startDate,
            salaryContractId,
            ESalaryMonthly.getValue(salaryStatus),
            isRemind,
            approver,
            duration,
            actualWorkingPoint,
            otPoint,
            standardPoint,
            finalSalary,
            totalAdvance,
            totalDeduction,
            totalBonus,
            totalInsurance,
            totalTax,
            totalAllowance)
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
        .set(SALARY_MONTHLY.TOTAL_ALLOWANCE, salaryMonthlyDto.getTotalAllowance())
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

  private int countAllPersonalSalaryMonthly(List<Condition> conditions) {
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

  private int countAllManagementSalaryMonthly(List<Condition> conditions, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> selectReview =
        dslContext.select(REVIEW_SALARY.SALARY_ID, REVIEW_SALARY.EMPLOYEE_ID).from(REVIEW_SALARY);
    return dslContext.fetchCount(
        dslContext
            .select(SALARY_MONTHLY.SALARY_ID)
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
            .and(SALARY_MONTHLY.APPROVER.eq(employeeId))
            .unionAll(
                dslContext
                    .select(SALARY_MONTHLY.SALARY_ID)
                    .from(SALARY_MONTHLY)
                    .leftJoin(SALARY_CONTRACT)
                    .on(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(SALARY_MONTHLY.SALARY_CONTRACT_ID))
                    .leftJoin(WORKING_CONTRACT)
                    .on(
                        WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(
                            SALARY_CONTRACT.WORKING_CONTRACT_ID))
                    .leftJoin(EMPLOYEE)
                    .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
                    .leftJoin(WORKING_PLACE)
                    .on(
                        WORKING_PLACE.WORKING_CONTRACT_ID.eq(
                            Tables.WORKING_CONTRACT.WORKING_CONTRACT_ID))
                    .leftJoin(JOB)
                    .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
                    .leftJoin(SALARY_STATUS)
                    .on(SALARY_STATUS.STATUS_ID.eq(SALARY_MONTHLY.SALARY_STATUS_ID))
                    .leftJoin(selectReview)
                    .on(selectReview.field(REVIEW_SALARY.SALARY_ID).eq(SALARY_MONTHLY.SALARY_ID))
                    .where(conditions)
                    .and(selectReview.field(REVIEW_SALARY.EMPLOYEE_ID).eq(employeeId))));
  }

  private Select<?> getAllManagementSalaryMonthly(
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination,
      String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> selectReview =
        dslContext.select(REVIEW_SALARY.SALARY_ID, REVIEW_SALARY.EMPLOYEE_ID).from(REVIEW_SALARY);
    return dslContext
        .select(
            SALARY_MONTHLY.SALARY_ID.as("salaryMonthlyId"),
            EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
            EMPLOYEE.FULL_NAME.as("fullName"),
            JOB.POSITION.as("position"),
            SALARY_MONTHLY.APPROVER.as("approverId"),
            SALARY_MONTHLY.STANDARD_POINT.as("standardPoint"),
            SALARY_MONTHLY.ACTUAL_POINT.as("actualPoint"),
            SALARY_MONTHLY.OT_POINT.as("otPoint"),
            SALARY_MONTHLY.TOTAL_DEDUCTION.as("totalDeduction"),
            SALARY_MONTHLY.TOTAL_BONUS.as("totalBonus"),
            SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT.as("totalInsurance"),
            SALARY_MONTHLY.TOTAL_TAX_PAYMENT.as("totalTax"),
            SALARY_MONTHLY.TOTAL_ADVANCE.as("totalAdvance"),
            SALARY_MONTHLY.TOTAL_ALLOWANCE.as("totalAllowance"),
            SALARY_MONTHLY.FINAL_SALARY.as("finalSalary"),
            SALARY_MONTHLY.START_DATE.as("startDate"),
            SALARY_MONTHLY.END_DATE.as("endDate"),
            SALARY_STATUS.NAME.as("salaryStatus"),
            SALARY_MONTHLY.COMMENT,
            SALARY_MONTHLY.SALARY_STATUS_ID)
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
        .and(SALARY_MONTHLY.APPROVER.eq(employeeId))
        .unionAll(
            dslContext
                .select(
                    SALARY_MONTHLY.SALARY_ID.as("salaryMonthlyId"),
                    EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
                    EMPLOYEE.FULL_NAME.as("fullName"),
                    JOB.POSITION.as("position"),
                    SALARY_MONTHLY.APPROVER.as("approverId"),
                    SALARY_MONTHLY.STANDARD_POINT.as("standardPoint"),
                    SALARY_MONTHLY.ACTUAL_POINT.as("actualPoint"),
                    SALARY_MONTHLY.OT_POINT.as("otPoint"),
                    SALARY_MONTHLY.TOTAL_DEDUCTION.as("totalDeduction"),
                    SALARY_MONTHLY.TOTAL_BONUS.as("totalBonus"),
                    SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT.as("totalInsurance"),
                    SALARY_MONTHLY.TOTAL_TAX_PAYMENT.as("totalTax"),
                    SALARY_MONTHLY.TOTAL_ADVANCE.as("totalAdvance"),
                    SALARY_MONTHLY.TOTAL_ALLOWANCE.as("totalAllowance"),
                    SALARY_MONTHLY.FINAL_SALARY.as("finalSalary"),
                    SALARY_MONTHLY.START_DATE.as("startDate"),
                    SALARY_MONTHLY.END_DATE.as("endDate"),
                    SALARY_STATUS.NAME.as("salaryStatus"),
                    SALARY_MONTHLY.COMMENT,
                    SALARY_MONTHLY.SALARY_STATUS_ID)
                .from(SALARY_MONTHLY)
                .leftJoin(SALARY_CONTRACT)
                .on(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(SALARY_MONTHLY.SALARY_CONTRACT_ID))
                .leftJoin(WORKING_CONTRACT)
                .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
                .leftJoin(EMPLOYEE)
                .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
                .leftJoin(WORKING_PLACE)
                .on(
                    WORKING_PLACE.WORKING_CONTRACT_ID.eq(
                        Tables.WORKING_CONTRACT.WORKING_CONTRACT_ID))
                .leftJoin(JOB)
                .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
                .leftJoin(SALARY_STATUS)
                .on(SALARY_STATUS.STATUS_ID.eq(SALARY_MONTHLY.SALARY_STATUS_ID))
                .leftJoin(selectReview)
                .on(selectReview.field(REVIEW_SALARY.SALARY_ID).eq(SALARY_MONTHLY.SALARY_ID))
                .where(conditions)
                .and(selectReview.field(REVIEW_SALARY.EMPLOYEE_ID).eq(employeeId)))
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  private Select<?> getAllPersonalSalaryMonthly(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            SALARY_MONTHLY.SALARY_ID.as("salaryMonthlyId"),
            EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
            EMPLOYEE.FULL_NAME.as("fullName"),
            JOB.POSITION.as("position"),
            SALARY_MONTHLY.APPROVER.as("approverId"),
            SALARY_MONTHLY.STANDARD_POINT.as("standardPoint"),
            SALARY_MONTHLY.ACTUAL_POINT.as("actualPoint"),
            SALARY_MONTHLY.OT_POINT.as("otPoint"),
            SALARY_MONTHLY.TOTAL_DEDUCTION.as("totalDeduction"),
            SALARY_MONTHLY.TOTAL_BONUS.as("totalBonus"),
            SALARY_MONTHLY.TOTAL_INSURANCE_PAYMENT.as("totalInsurance"),
            SALARY_MONTHLY.TOTAL_TAX_PAYMENT.as("totalTax"),
            SALARY_MONTHLY.TOTAL_ADVANCE.as("totalAdvance"),
            SALARY_MONTHLY.TOTAL_ALLOWANCE.as("totalAllowance"),
            SALARY_MONTHLY.FINAL_SALARY.as("finalSalary"),
            SALARY_MONTHLY.START_DATE.as("startDate"),
            SALARY_MONTHLY.END_DATE.as("endDate"),
            SALARY_STATUS.NAME.as("salaryStatus"),
            SALARY_MONTHLY.COMMENT)
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

  private Select<?> getListForwarderBySalaryId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(REVIEW_SALARY.EMPLOYEE_ID)
        .from(REVIEW_SALARY)
        .where(REVIEW_SALARY.SALARY_ID.eq(salaryId));
  }
}