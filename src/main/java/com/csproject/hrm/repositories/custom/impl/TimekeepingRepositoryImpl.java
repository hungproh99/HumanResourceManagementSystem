package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.common.enums.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
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
                          ETimekeepingStatus.getValue(timekeepingStatus.getTimekeeping_status()));
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
                          ETimekeepingStatus.getValue(timekeepingStatus.getTimekeeping_status()));
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
                TIMEKEEPING_STATUS.NAME.as("timekeeping_status_name"))
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
  public void insertTimekeepingByEmployeeId(String employeeId, LocalDate date) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    final var query =
        dslContext
            .insertInto(TIMEKEEPING, TIMEKEEPING.EMPLOYEE_ID, TIMEKEEPING.DATE)
            .values(employeeId, date)
            .execute();
  }

  @Override
  public void insertTimekeepingStatusByEmployeeIdAndRangeDate(
      String employeeId, LocalDate startDate, LocalDate endDate, long timekeepingStatus) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Long> timekeepingIdList =
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
            .and(TIMEKEEPING.DATE.between(startDate, endDate))
            .fetchInto(Long.class);

    dslContext.transaction(
        configuration -> {
          timekeepingIdList.forEach(
              timekeepingId -> {
                queries.add(
                    dslContext
                        .insertInto(
                            LIST_TIMEKEEPING_STATUS,
                            LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID,
                            LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID)
                        .values(timekeepingId, timekeepingStatus));
              });
          DSL.using(configuration).batch(queries).execute();
        });
  }

  @Override
  public void updateTimekeepingStatusByEmployeeIdAndRangeDate(
      String employeeId,
      LocalDate startDate,
      LocalDate endDate,
      long oldTimekeepingStatus,
      long newTimekeepingStatus) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Long> timekeepingIdList =
        dslContext
            .select(TIMEKEEPING.TIMEKEEPING_ID)
            .from(TIMEKEEPING)
            .where(TIMEKEEPING.EMPLOYEE_ID.eq(employeeId))
            .and(TIMEKEEPING.DATE.between(startDate, endDate))
            .fetchInto(Long.class);

    dslContext.transaction(
        configuration -> {
          timekeepingIdList.forEach(
              timekeepingId -> {
                queries.add(
                    dslContext
                        .update(LIST_TIMEKEEPING_STATUS)
                        .set(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID, newTimekeepingStatus)
                        .where(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(timekeepingId))
                        .and(
                            LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID.eq(
                                oldTimekeepingStatus)));
              });
          DSL.using(configuration).batch(queries).execute();
        });
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
}