package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.CheckInCheckOutResponse;
import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponse;
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
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
import static org.jooq.codegen.maven.example.tables.Timekeeping.TIMEKEEPING;
import static org.jooq.codegen.maven.example.tables.TimekeepingStatus.TIMEKEEPING_STATUS;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
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
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<TimekeepingResponse> getListAllTimekeeping(QueryParam queryParam) {
    final List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          Condition officeCondition = DSL.noCondition();
          Condition areaCondition = DSL.noCondition();
          Condition positionCondition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(Constants.OFFICE)) {
              officeCondition =
                  officeCondition.or(OFFICE.NAME.upper().eq(filter.condition.toUpperCase()));
            } else if (filter.field.equals(Constants.AREA)) {
              areaCondition =
                  areaCondition.or(AREA.NAME.upper().eq(filter.condition.toUpperCase()));
            } else if (filter.field.equals(POSITION)) {
              positionCondition =
                  positionCondition.or(JOB.POSITION.upper().eq(filter.condition.toUpperCase()));
            }

            final var newCondition = queryHelper.condition(filter, field);
            condition = condition.and(newCondition);
          }
          condition = condition.and(officeCondition).and(areaCondition).and(positionCondition);
          conditions.add(condition);
        });
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    return getListAllTimekeeping(conditions, sortFields, queryParam.pagination)
        .fetchInto(TimekeepingResponse.class);
  }

  @Override
  public List<TimekeepingResponse> getListTimekeepingToExport(List<String> list) {
    List<Condition> conditions = new ArrayList<>();
    Condition condition = noCondition();
    for (String id : list) {
      condition = condition.or(EMPLOYEE.EMPLOYEE_ID.eq(id));
    }
    conditions.add(condition);
    return getTimekeepingToExport(conditions).fetchInto(TimekeepingResponse.class);
  }

  private Select<?> getListAllTimekeeping(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> firstTimeCheckIn =
        dslContext.select().from(CHECKIN_CHECKOUT).orderBy(CHECKIN_CHECKOUT.CHECKIN.asc()).limit(1);
    TableLike<?> lastTimeCheckOut =
        dslContext
            .select()
            .from(CHECKIN_CHECKOUT)
            .orderBy(CHECKIN_CHECKOUT.CHECKIN.desc())
            .limit(1);
    return dslContext
        .select(
            EMPLOYEE.FULL_NAME,
            JOB.POSITION,
            GRADE_TYPE.NAME.as(GRADE),
            TIMEKEEPING.DATE.as(CURRENT_DATE),
            TIMEKEEPING_STATUS.NAME.as(Constants.TIMEKEEPING_STATUS),
            firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN).as(FIRST_CHECK_IN),
            lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT).as(LAST_CHECK_OUT))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_CONTRACT.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_CONTRACT.OFFICE_ID))
        .leftJoin(TIMEKEEPING)
        .on(TIMEKEEPING.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(TIMEKEEPING_STATUS)
        .on(TIMEKEEPING_STATUS.TYPE_ID.eq(TIMEKEEPING.TIMEKEEPING_STATUS_ID))
        .leftJoin(firstTimeCheckIn)
        .on(firstTimeCheckIn.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID).eq(TIMEKEEPING.TIMEKEEPING_ID))
        .leftJoin(lastTimeCheckOut)
        .on(lastTimeCheckOut.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID).eq(TIMEKEEPING.TIMEKEEPING_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  private Select<?> getTimekeepingToExport(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> firstTimeCheckIn =
        dslContext.select().from(CHECKIN_CHECKOUT).orderBy(CHECKIN_CHECKOUT.CHECKIN.asc()).limit(1);
    TableLike<?> lastTimeCheckOut =
        dslContext
            .select()
            .from(CHECKIN_CHECKOUT)
            .orderBy(CHECKIN_CHECKOUT.CHECKIN.desc())
            .limit(1);
    return dslContext
        .select(
            EMPLOYEE.FULL_NAME,
            JOB.POSITION,
            GRADE_TYPE.NAME.as(GRADE),
            TIMEKEEPING.DATE.as(CURRENT_DATE),
            TIMEKEEPING_STATUS.NAME.as(Constants.TIMEKEEPING_STATUS),
            firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN).as(FIRST_CHECK_IN),
            lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT).as(LAST_CHECK_OUT))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
        .leftJoin(TIMEKEEPING)
        .on(TIMEKEEPING.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(TIMEKEEPING_STATUS)
        .on(TIMEKEEPING_STATUS.TYPE_ID.eq(TIMEKEEPING.TIMEKEEPING_STATUS_ID))
        .leftJoin(firstTimeCheckIn)
        .on(firstTimeCheckIn.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID).eq(TIMEKEEPING.TIMEKEEPING_ID))
        .leftJoin(lastTimeCheckOut)
        .on(lastTimeCheckOut.field(CHECKIN_CHECKOUT.TIMEKEEPING_ID).eq(TIMEKEEPING.TIMEKEEPING_ID))
        .where(conditions);
  }

  @Override
  public List<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, LocalDate date) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Condition> conditions = new ArrayList<>();
    Condition condition = noCondition();
    conditions.add(condition.and(EMPLOYEE.EMPLOYEE_ID.eq(employeeID)));
    conditions.add(condition.and(TIMEKEEPING.DATE.eq(date)));

    TableLike<?> firstTimeCheckIn =
        dslContext.select().from(CHECKIN_CHECKOUT).orderBy(CHECKIN_CHECKOUT.CHECKIN.asc()).limit(1);
    TableLike<?> lastTimeCheckOut =
        dslContext
            .select()
            .from(CHECKIN_CHECKOUT)
            .orderBy(CHECKIN_CHECKOUT.CHECKIN.desc())
            .limit(1);
    final var query =
        dslContext
            .select(
                TIMEKEEPING.TIMEKEEPING_ID,
                EMPLOYEE.EMPLOYEE_ID,
                EMPLOYEE.FULL_NAME,
                EMPLOYEE.MANAGER_ID,
                JOB.POSITION,
                GRADE_TYPE.NAME.as(GRADE),
                TIMEKEEPING.DATE.as(CURRENT_DATE),
                TIMEKEEPING_STATUS.NAME.as(Constants.TIMEKEEPING_STATUS),
                (hour(lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT))
                        .multiply(60)
                        .add(minute(lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT)))
                        .minus(
                            hour(firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN))
                                .multiply(60)
                                .add(minute(firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN)))))
                    .divide(60)
                    .cast(SQLDataType.DECIMAL(4, 2))
                    .as("total_working_time"),
                firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN).as(FIRST_CHECK_IN),
                lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT).as(LAST_CHECK_OUT))
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(JOB)
            .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
            .leftJoin(GRADE_TYPE)
            .on(GRADE_TYPE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
            .leftJoin(TIMEKEEPING)
            .on(TIMEKEEPING.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(TIMEKEEPING_STATUS)
            .on(TIMEKEEPING_STATUS.TYPE_ID.eq(TIMEKEEPING.TIMEKEEPING_STATUS_ID))
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
    return query.fetchInto(TimekeepingDetailResponse.class);
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
}
