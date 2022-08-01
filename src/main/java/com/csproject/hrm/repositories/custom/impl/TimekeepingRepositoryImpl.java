package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.common.enums.EGradeType;
import com.csproject.hrm.common.enums.EJob;
import com.csproject.hrm.common.enums.ETimekeepingStatus;
import com.csproject.hrm.dto.dto.TimekeepingDto;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.TimekeepingRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.OVERTIME;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.CheckinCheckout.CHECKIN_CHECKOUT;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.GradeType.GRADE_TYPE;
import static org.jooq.codegen.maven.example.tables.Job.JOB;
import static org.jooq.codegen.maven.example.tables.ListTimekeepingStatus.LIST_TIMEKEEPING_STATUS;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
import static org.jooq.codegen.maven.example.tables.Timekeeping.TIMEKEEPING;
import static org.jooq.codegen.maven.example.tables.TimekeepingStatus.TIMEKEEPING_STATUS;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.codegen.maven.example.tables.WorkingPlace.WORKING_PLACE;
import static org.jooq.impl.DSL.*;

@AllArgsConstructor
public class TimekeepingRepositoryImpl implements TimekeepingRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
    field2Map.put(FULL_NAME, EMPLOYEE.FULL_NAME);
    field2Map.put(POSITION, JOB.POSITION);
    field2Map.put(Constants.OFFICE, OFFICE.NAME);
    field2Map.put(Constants.AREA, AREA.NAME);
    field2Map.put(DATE, TIMEKEEPING.DATE);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<TimekeepingResponses> getListAllTimekeeping(QueryParam queryParam) {
    final List<Condition> conditions = new ArrayList<>();
    final List<Condition> conditionsTimekeeping = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          Condition officeCondition = DSL.noCondition();
          Condition areaCondition = DSL.noCondition();
          Condition positionCondition = DSL.noCondition();
          Condition timekeepingCondition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(Constants.OFFICE)) {
              officeCondition = officeCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(Constants.AREA)) {
              areaCondition = areaCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(POSITION)) {
              positionCondition = positionCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(DATE)) {
              timekeepingCondition = timekeepingCondition.and(queryHelper.condition(filter, field));
            } else {
              condition = condition.and(queryHelper.condition(filter, field));
            }
          }
          condition = condition.and(officeCondition).and(areaCondition).and(positionCondition);
          conditions.add(condition);
          conditionsTimekeeping.add(timekeepingCondition);
        });
    List<OrderField<?>> sortFields = new ArrayList<>();
    sortFields.add(EMPLOYEE.EMPLOYEE_ID.asc());

    List<TimekeepingResponses> timekeepingResponses =
        getAllEmployeeForTimekeeping(conditions, sortFields, queryParam.pagination)
            .fetchInto(TimekeepingResponses.class);
    timekeepingResponses.forEach(
        timekeepingResponse -> {
          timekeepingResponse.setGrade(EGradeType.getLabel(timekeepingResponse.getGrade()));
          timekeepingResponse.setPosition(EJob.getLabel(timekeepingResponse.getPosition()));
          List<TimekeepingResponse> timekeepingList =
              getAllTimekeepingByEmployeeId(
                      timekeepingResponse.getEmployee_id(), conditionsTimekeeping, sortFields)
                  .fetchInto(TimekeepingResponse.class);
          timekeepingList.forEach(
              timekeeping -> {
                List<ListTimekeepingStatusResponse> timekeepingStatusList =
                    getAllTimekeepingStatusByTimekeepingId(
                            timekeeping.getTimekeeping_id(), conditionsTimekeeping, sortFields)
                        .fetchInto(ListTimekeepingStatusResponse.class);
                timekeepingStatusList.forEach(
                    timekeepingStatus -> {
                      timekeepingStatus.setTimekeeping_status(
                          ETimekeepingStatus.getLabel(timekeepingStatus.getTimekeeping_status()));
                    });
                timekeeping.setTimekeeping_status(timekeepingStatusList);
              });
          timekeepingResponse.setTimekeepingResponses(timekeepingList);
        });
    return timekeepingResponses;
  }

  @Override
  public List<TimekeepingResponses> getListTimekeepingToExport(
      QueryParam queryParam, List<String> list) {
    final List<Condition> conditions = new ArrayList<>();
    final List<Condition> conditionsTimekeeping = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          Condition officeCondition = DSL.noCondition();
          Condition areaCondition = DSL.noCondition();
          Condition positionCondition = DSL.noCondition();
          Condition timekeepingCondition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(Constants.OFFICE)) {
              officeCondition = officeCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(Constants.AREA)) {
              areaCondition = areaCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(POSITION)) {
              positionCondition = positionCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(DATE)) {
              timekeepingCondition = timekeepingCondition.and(queryHelper.condition(filter, field));
            } else {
              condition = condition.and(queryHelper.condition(filter, field));
            }
          }
          condition = condition.and(officeCondition).and(areaCondition).and(positionCondition);
          conditions.add(condition);
          conditionsTimekeeping.add(timekeepingCondition);
        });

    List<OrderField<?>> sortFields = new ArrayList<>();
    sortFields.add(EMPLOYEE.EMPLOYEE_ID.asc());

    Condition condition = noCondition();
    for (String id : list) {
      condition = condition.or(EMPLOYEE.EMPLOYEE_ID.eq(id));
    }
    conditions.add(condition);

    List<TimekeepingResponses> timekeepingResponses =
        getAllEmployeeForTimekeeping(conditions, sortFields, queryParam.pagination)
            .fetchInto(TimekeepingResponses.class);
    timekeepingResponses.forEach(
        timekeepingResponse -> {
          timekeepingResponse.setGrade(EGradeType.getLabel(timekeepingResponse.getGrade()));
          timekeepingResponse.setPosition(EJob.getLabel(timekeepingResponse.getPosition()));
          List<TimekeepingResponse> timekeepingList =
              getAllTimekeepingByEmployeeId(
                      timekeepingResponse.getEmployee_id(), conditionsTimekeeping, sortFields)
                  .fetchInto(TimekeepingResponse.class);
          timekeepingList.forEach(
              timekeeping -> {
                List<ListTimekeepingStatusResponse> timekeepingStatusList =
                    getAllTimekeepingStatusByTimekeepingId(
                            timekeeping.getTimekeeping_id(), conditionsTimekeeping, sortFields)
                        .fetchInto(ListTimekeepingStatusResponse.class);
                timekeepingStatusList.forEach(
                    timekeepingStatus -> {
                      timekeepingStatus.setTimekeeping_status(
                          ETimekeepingStatus.getLabel(timekeepingStatus.getTimekeeping_status()));
                    });
                timekeeping.setTimekeeping_status(timekeepingStatusList);
              });
          timekeepingResponse.setTimekeepingResponses(timekeepingList);
        });
    return timekeepingResponses;
  }

  @Override
  public Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, LocalDate date) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Condition> conditions = new ArrayList<>();
    Condition condition = noCondition();
    conditions.add(condition.and(EMPLOYEE.EMPLOYEE_ID.eq(employeeID)));
    conditions.add(condition.and(TIMEKEEPING.DATE.eq(date)));

    TableLike<?> rowNumberAsc =
        dslContext
            .select(
                asterisk(),
                rowNumber()
                    .over()
                    .partitionBy(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .orderBy(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.asc())
                    .as("rowNumber"))
            .from(CHECKIN_CHECKOUT);

    TableLike<?> rowNumberDesc =
        dslContext
            .select(
                asterisk(),
                rowNumber()
                    .over()
                    .partitionBy(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .orderBy(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.desc())
                    .as("rowNumber"))
            .from(CHECKIN_CHECKOUT);

    TableLike<?> firstTimeCheckIn =
        dslContext
            .select()
            .from(rowNumberAsc)
            .where(rowNumberAsc.field("rowNumber").cast(Integer.class).eq(1));
    TableLike<?> lastTimeCheckOut =
        dslContext
            .select()
            .from(rowNumberDesc)
            .where(rowNumberDesc.field("rowNumber").cast(Integer.class).eq(1));
    final var query =
        dslContext
            .select(
                TIMEKEEPING.TIMEKEEPING_ID,
                EMPLOYEE.EMPLOYEE_ID,
                TIMEKEEPING.DATE.as(CURRENT_DATE),
                floor(
                        (hour(lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT))
                                .multiply(60)
                                .add(minute(lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT)))
                                .minus(
                                    hour(firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN))
                                        .multiply(60)
                                        .add(
                                            minute(
                                                firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN)))))
                            .divide(60))
                    .cast(SQLDataType.INTEGER)
                    .concat(".")
                    .concat(
                        (hour(lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT))
                                .multiply(60)
                                .add(minute(lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT)))
                                .minus(
                                    hour(firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN))
                                        .multiply(60)
                                        .add(
                                            minute(
                                                firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN)))))
                            .modulo(60)
                            .cast(SQLDataType.INTEGER))
                    .as(TOTAL_WORKING_TIME),
                firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN).as(FIRST_CHECK_IN),
                lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT).as(LAST_CHECK_OUT))
            .from(EMPLOYEE)
            .leftJoin(TIMEKEEPING)
            .on(TIMEKEEPING.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(firstTimeCheckIn)
            .on(
                firstTimeCheckIn
                    .field(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .eq(TIMEKEEPING.TIMEKEEPING_ID))
            .leftJoin(lastTimeCheckOut)
            .on(
                lastTimeCheckOut
                    .field(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .eq(TIMEKEEPING.TIMEKEEPING_ID))
            .where(conditions);
    return query.fetchOptionalInto(TimekeepingDetailResponse.class);
  }

  @Override
  public List<ListTimekeepingStatusResponse> getListTimekeepingStatus(Long timekeepingID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID,
                LIST_TIMEKEEPING_STATUS.LIST_ID,
                TIMEKEEPING_STATUS.NAME.as(Constants.TIMEKEEPING_STATUS))
            .from(LIST_TIMEKEEPING_STATUS)
            .leftJoin(TIMEKEEPING_STATUS)
            .on(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID.eq(TIMEKEEPING_STATUS.TYPE_ID))
            .where(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(timekeepingID));
    return query.fetchInto(ListTimekeepingStatusResponse.class);
  }

  @Override
  public List<CheckInCheckOutResponse> getCheckInCheckOutByTimekeepingID(Long timekeepingID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                CHECKIN_CHECKOUT.TIMEKEEPING_ID,
                CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID,
                CHECKIN_CHECKOUT.CHECKIN,
                CHECKIN_CHECKOUT.CHECKOUT)
            .from(CHECKIN_CHECKOUT)
            .where(CHECKIN_CHECKOUT.TIMEKEEPING_ID.eq(timekeepingID));
    return query.fetchInto(CheckInCheckOutResponse.class);
  }

  @Override
  public int countListAllTimekeeping(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = new ArrayList<>();
    final List<Condition> conditionsTimekeeping = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          Condition officeCondition = DSL.noCondition();
          Condition areaCondition = DSL.noCondition();
          Condition positionCondition = DSL.noCondition();
          Condition timekeepingCondition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(Constants.OFFICE)) {
              officeCondition = officeCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(Constants.AREA)) {
              areaCondition = areaCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(POSITION)) {
              positionCondition = positionCondition.or(queryHelper.condition(filter, field));
            } else if (filter.field.equals(DATE)) {
              timekeepingCondition = timekeepingCondition.and(queryHelper.condition(filter, field));
            } else {
              condition = condition.and(queryHelper.condition(filter, field));
            }
          }
          condition = condition.and(officeCondition).and(areaCondition).and(positionCondition);
          conditions.add(condition);
          conditionsTimekeeping.add(timekeepingCondition);
        });

    return dslContext.fetchCount(getCountAllEmployeeForTimekeeping(conditions));
  }

  @Override
  public void insertTimekeepingByEmployeeId(
      String employeeId, LocalDate startDate, LocalDate endDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<LocalDate> listOfDates = startDate.datesUntil(endDate).collect(Collectors.toList());
    List<Query> queries = new ArrayList<>();
    dslContext.transaction(
        configuration -> {
          listOfDates.forEach(
              date -> {
                queries.add(
                    dslContext
                        .insertInto(TIMEKEEPING, TIMEKEEPING.EMPLOYEE_ID, TIMEKEEPING.DATE)
                        .values(employeeId, date));
              });
          DSL.using(configuration).batch(queries).execute();
        });
  }

  @Override
  public void upsertTimekeepingStatusByEmployeeIdAndRangeDate(
      String employeeId,
      LocalDate startDate,
      LocalDate endDate,
      long oldTimekeepingStatus,
      long newTimekeepingStatus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Query> queries = new ArrayList<>();
    List<Long> timekeepingIdList =
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
            .and(TIMEKEEPING.DATE.ge(startDate))
            .and(TIMEKEEPING.DATE.le(endDate))
            .fetchInto(Long.class);

    dslContext.transaction(
        configuration -> {
          timekeepingIdList.forEach(
              timekeepingId -> {
                boolean checkExist =
                    dslContext.fetchExists(
                        select(LIST_TIMEKEEPING_STATUS.LIST_ID)
                            .from(LIST_TIMEKEEPING_STATUS)
                            .where(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(timekeepingId))
                            .and(
                                LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID.eq(
                                    oldTimekeepingStatus)));
                if (checkExist && oldTimekeepingStatus != newTimekeepingStatus) {
                  queries.add(
                      dslContext
                          .update(LIST_TIMEKEEPING_STATUS)
                          .set(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID, newTimekeepingStatus)
                          .where(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(timekeepingId))
                          .and(
                              LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID.eq(
                                  oldTimekeepingStatus)));
                } else if (!checkExist && oldTimekeepingStatus == newTimekeepingStatus) {
                  queries.add(
                      dslContext
                          .insertInto(
                              LIST_TIMEKEEPING_STATUS,
                              LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID,
                              LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID)
                          .values(timekeepingId, newTimekeepingStatus));
                }
              });
          DSL.using(configuration).batch(queries).execute();
        });
  }

  @Override
  public void deleteTimekeepingStatusByEmployeeIdAndDate(
      String employeeId, LocalDate date, long oldTimekeepingStatus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    Long timekeepingId =
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
            .and(TIMEKEEPING.DATE.eq(date))
            .fetchOneInto(Long.class);
    final var query =
        dslContext
            .delete(LIST_TIMEKEEPING_STATUS)
            .where(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(timekeepingId))
            .and(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID.eq(oldTimekeepingStatus))
            .execute();
  }

  private Select<?> getCountAllEmployeeForTimekeeping(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(EMPLOYEE.EMPLOYEE_ID, EMPLOYEE.FULL_NAME, JOB.POSITION, GRADE_TYPE.NAME.as(GRADE))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
        .where(conditions)
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .orderBy(EMPLOYEE.EMPLOYEE_ID.asc());
  }

  private Select<?> getAllEmployeeForTimekeeping(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(EMPLOYEE.EMPLOYEE_ID, EMPLOYEE.FULL_NAME, JOB.POSITION, GRADE_TYPE.NAME.as(GRADE))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
        .where(conditions)
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  private Select<?> getAllTimekeepingByEmployeeId(
      String employeeId, List<Condition> conditions, List<OrderField<?>> sortFields) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    sortFields.add(TIMEKEEPING.DATE.asc());
    TableLike<?> rowNumberAsc =
        dslContext
            .select(
                asterisk(),
                rowNumber()
                    .over()
                    .partitionBy(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .orderBy(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.asc())
                    .as("rowNumber"))
            .from(CHECKIN_CHECKOUT);

    TableLike<?> rowNumberDesc =
        dslContext
            .select(
                asterisk(),
                rowNumber()
                    .over()
                    .partitionBy(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .orderBy(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.desc())
                    .as("rowNumber"))
            .from(CHECKIN_CHECKOUT);

    TableLike<?> firstTimeCheckIn =
        dslContext
            .select()
            .from(rowNumberAsc)
            .where(rowNumberAsc.field("rowNumber").cast(Integer.class).eq(1));
    TableLike<?> lastTimeCheckOut =
        dslContext
            .select()
            .from(rowNumberDesc)
            .where(rowNumberDesc.field("rowNumber").cast(Integer.class).eq(1));
    return dslContext
        .select(
            TIMEKEEPING.TIMEKEEPING_ID,
            TIMEKEEPING.DATE.as(CURRENT_DATE),
            firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN).as(FIRST_CHECK_IN),
            lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT).as(LAST_CHECK_OUT))
        .from(TIMEKEEPING)
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(TIMEKEEPING.EMPLOYEE_ID))
        .leftJoin(firstTimeCheckIn)
        .on(firstTimeCheckIn.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID).eq(TIMEKEEPING.TIMEKEEPING_ID))
        .leftJoin(lastTimeCheckOut)
        .on(lastTimeCheckOut.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID).eq(TIMEKEEPING.TIMEKEEPING_ID))
        .where(conditions)
        .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
        .orderBy(sortFields);
  }

  private Select<?> getAllTimekeepingStatusByTimekeepingId(
      long timekeepingId, List<Condition> conditions, List<OrderField<?>> sortFields) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            LIST_TIMEKEEPING_STATUS.LIST_ID,
            TIMEKEEPING_STATUS.NAME.as(Constants.TIMEKEEPING_STATUS))
        .from(LIST_TIMEKEEPING_STATUS)
        .leftJoin(TIMEKEEPING_STATUS)
        .on(TIMEKEEPING_STATUS.TYPE_ID.eq(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID))
        .leftJoin(TIMEKEEPING)
        .on(TIMEKEEPING.TIMEKEEPING_ID.eq(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID))
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(TIMEKEEPING.EMPLOYEE_ID))
        .where(conditions)
        .and(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(timekeepingId))
        .orderBy(sortFields);
  }

  @Override
  public void insertOvertimeByEmployeeIdAndRangeDate(
      String employeeId,
      LocalDate startDate,
      LocalDate endDate,
      LocalTime startTime,
      LocalTime endTime,
      Long overtimeType) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Long> timekeepingIdList =
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
            .and(TIMEKEEPING.DATE.ge(startDate))
            .and(TIMEKEEPING.DATE.le(endDate))
            .fetchInto(Long.class);

    dslContext.transaction(
        configuration -> {
          timekeepingIdList.forEach(
              timekeepingId -> {
                queries.add(
                    dslContext
                        .insertInto(
                            OVERTIME,
                            OVERTIME.TIMEKEEPING_ID,
                            OVERTIME.START_TIME,
                            OVERTIME.END_TIME,
                            OVERTIME.OVERTIME_TYPE_ID)
                        .values(timekeepingId, startTime, endTime, overtimeType));
              });
          DSL.using(configuration).batch(queries).execute();
        });
  }

  @Override
  public Long insertTimekeeping(TimekeepingDto timekeepingDto) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var insertTimekeeping =
        dslContext
            .insertInto(
                TIMEKEEPING,
                TIMEKEEPING.POINT_WORK_DAY,
                TIMEKEEPING.POINT_OT_DAY,
                TIMEKEEPING.DATE,
                TIMEKEEPING.EMPLOYEE_ID)
            .values(
                timekeepingDto.getPointWorkDay(),
                timekeepingDto.getPointOTDay(),
                timekeepingDto.getDate(),
                timekeepingDto.getEmployeeId())
            .returningResult(TIMEKEEPING.TIMEKEEPING_ID)
            .fetchOne();
    return insertTimekeeping.getValue(TIMEKEEPING.TIMEKEEPING_ID);
  }

  @Override
  public void updatePointPerDay(List<TimekeepingDto> timekeepingDtoList) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var queries =
        timekeepingDtoList.stream()
            .map(
                timekeepingDto ->
                    dslContext
                        .update(TIMEKEEPING)
                        .set(TIMEKEEPING.POINT_WORK_DAY, timekeepingDto.getPointWorkDay())
                        .set(TIMEKEEPING.POINT_OT_DAY, timekeepingDto.getPointOTDay())
                        .where(TIMEKEEPING.EMPLOYEE_ID.eq(timekeepingDto.getEmployeeId()))
                        .and(TIMEKEEPING.DATE.eq(timekeepingDto.getDate())))
            .toArray(Query[]::new);
    dslContext.batch(queries).execute();
  }

  @Override
  public boolean checkExistDateInTimekeeping(LocalDate date, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
            .and(TIMEKEEPING.DATE.eq(date)));
  }

  @Override
  public LocalTime getFirstTimeCheckInByTimekeeping(LocalDate date, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> rowNumberAsc =
        dslContext
            .select(
                asterisk(),
                rowNumber()
                    .over()
                    .partitionBy(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .orderBy(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.asc())
                    .as("rowNumber"))
            .from(CHECKIN_CHECKOUT);

    return dslContext
        .select(rowNumberAsc.field(CHECKIN_CHECKOUT.CHECKIN))
        .from(rowNumberAsc)
        .leftJoin(TIMEKEEPING)
        .on(TIMEKEEPING.TIMEKEEPING_ID.eq(rowNumberAsc.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID)))
        .where(rowNumberAsc.field("rowNumber").cast(Integer.class).eq(1))
        .and(TIMEKEEPING.DATE.eq(date))
        .and(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
        .fetchOneInto(LocalTime.class);
  }

  @Override
  public LocalTime getLastTimeCheckOutByTimekeeping(LocalDate date, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> rowNumberDesc =
        dslContext
            .select(
                asterisk(),
                rowNumber()
                    .over()
                    .partitionBy(CHECKIN_CHECKOUT.TIMEKEEPING_ID)
                    .orderBy(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.desc())
                    .as("rowNumber"))
            .from(CHECKIN_CHECKOUT);

    return dslContext
        .select(rowNumberDesc.field(CHECKIN_CHECKOUT.CHECKOUT))
        .from(rowNumberDesc)
        .leftJoin(TIMEKEEPING)
        .on(TIMEKEEPING.TIMEKEEPING_ID.eq(rowNumberDesc.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID)))
        .where(rowNumberDesc.field("rowNumber").cast(Integer.class).eq(1))
        .and(TIMEKEEPING.DATE.eq(date))
        .and(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
        .fetchOneInto(LocalTime.class);
  }

  @Override
  public int countTimeDayOffPerMonthByEmployeeId(
      LocalDate firstDate, LocalDate lastDate, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(TIMEKEEPING.EMPLOYEE_ID))
            .leftJoin(LIST_TIMEKEEPING_STATUS)
            .on(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(TIMEKEEPING.TIMEKEEPING_ID))
            .leftJoin(TIMEKEEPING_STATUS)
            .on(TIMEKEEPING_STATUS.TYPE_ID.eq(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID))
            .where(TIMEKEEPING_STATUS.NAME.eq(ETimekeepingStatus.DAY_OFF.name()))
            .and(TIMEKEEPING.DATE.ge(firstDate))
            .and(TIMEKEEPING.DATE.le(lastDate))
            .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId)));
  }

  @Override
  public int countTimePaidLeavePerMonthByEmployeeId(
      LocalDate firstDate, LocalDate lastDate, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(TIMEKEEPING.EMPLOYEE_ID))
            .leftJoin(LIST_TIMEKEEPING_STATUS)
            .on(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(TIMEKEEPING.TIMEKEEPING_ID))
            .leftJoin(TIMEKEEPING_STATUS)
            .on(TIMEKEEPING_STATUS.TYPE_ID.eq(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID))
            .where(TIMEKEEPING_STATUS.NAME.eq(ETimekeepingStatus.PAID_LEAVE.name()))
            .and(TIMEKEEPING.DATE.ge(firstDate))
            .and(TIMEKEEPING.DATE.le(lastDate))
            .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId)));
  }

  @Override
  public Double countPointDayWorkPerMonthByEmployeeId(
      LocalDate firstDate, LocalDate lastDate, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(sum(TIMEKEEPING.POINT_WORK_DAY))
        .from(TIMEKEEPING)
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(TIMEKEEPING.EMPLOYEE_ID))
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
        .and(TIMEKEEPING.DATE.ge(firstDate))
        .and(TIMEKEEPING.DATE.le(lastDate))
        .fetchOneInto(Double.class);
  }

  @Override
  public int countPaidLeaveOfEmployeeByYear(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .leftJoin(LIST_TIMEKEEPING_STATUS)
            .on(TIMEKEEPING.TIMEKEEPING_ID.eq(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID))
            .leftJoin(TIMEKEEPING_STATUS)
            .on(TIMEKEEPING_STATUS.TYPE_ID.eq(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID))
            .where(TIMEKEEPING.EMPLOYEE_ID.equalIgnoreCase(employeeID))
            .and(year(TIMEKEEPING.DATE).eq(LocalDate.now().getYear()))
            .and(TIMEKEEPING_STATUS.NAME.eq("PAID_LEAVE")));
  }

  @Override
  public Integer countOvertimeOfEmployeeByMonth(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    Integer total = 0;

    List<Integer> list =
        dslContext
            .select(
                ((hour(OVERTIME.END_TIME).multiply(60).add(minute(OVERTIME.END_TIME)))
                        .minus(
                            (hour(OVERTIME.START_TIME))
                                .multiply(60)
                                .add(minute(OVERTIME.START_TIME))))
                    .cast(SQLDataType.INTEGER))
            .from(OVERTIME)
            .leftJoin(TIMEKEEPING)
            .on(OVERTIME.TIMEKEEPING_ID.eq(TIMEKEEPING.TIMEKEEPING_ID))
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeID))
            .and(year(TIMEKEEPING.DATE).eq(LocalDate.now().getYear()))
            .and(month(TIMEKEEPING.DATE).eq(LocalDate.now().getMonthValue()))
            .fetchInto(Integer.class);

    for (Integer i : list) {
      total = total + i;
    }

    return total;
  }

  @Override
  public Integer countOvertimeOfEmployeeByYear(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    Integer total = 0;

    List<Integer> list =
        dslContext
            .select(
                ((hour(OVERTIME.END_TIME).multiply(60).add(minute(OVERTIME.END_TIME)))
                        .minus(
                            (hour(OVERTIME.START_TIME))
                                .multiply(60)
                                .add(minute(OVERTIME.START_TIME))))
                    .cast(SQLDataType.INTEGER))
            .from(OVERTIME)
            .leftJoin(TIMEKEEPING)
            .on(OVERTIME.TIMEKEEPING_ID.eq(TIMEKEEPING.TIMEKEEPING_ID))
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeID))
            .and(year(TIMEKEEPING.DATE).eq(LocalDate.now().getYear()))
            .fetchInto(Integer.class);

    for (Integer i : list) {
      total = total + i;
    }

    return total;
  }
}