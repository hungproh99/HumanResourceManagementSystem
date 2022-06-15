package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.jooq.Pagination;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.custom.TimekeepingRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.CheckinCheckout.CHECKIN_CHECKOUT;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Grade.GRADE;
import static org.jooq.codegen.maven.example.tables.Job.JOB;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
import static org.jooq.codegen.maven.example.tables.Timekeeping.TIMEKEEPING;
import static org.jooq.codegen.maven.example.tables.TimekeepingStatus.TIMEKEEPING_STATUS;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.noCondition;

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
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
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
            GRADE.NAME.as(Constants.GRADE),
            TIMEKEEPING.DATE.as(CURRENT_DATE),
            TIMEKEEPING_STATUS.NAME.as(Constants.TIMEKEEPING_STATUS),
            firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN).as(FIRST_CHECK_IN),
            lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT).as(LAST_CHECK_OUT))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
        .leftJoin(GRADE)
        .on(GRADE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
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
            GRADE.NAME.as(Constants.GRADE),
            TIMEKEEPING.DATE.as(CURRENT_DATE),
            TIMEKEEPING_STATUS.NAME.as(Constants.TIMEKEEPING_STATUS),
            firstTimeCheckIn.field(CHECKIN_CHECKOUT.CHECKIN).as(FIRST_CHECK_IN),
            lastTimeCheckOut.field(CHECKIN_CHECKOUT.CHECKOUT).as(LAST_CHECK_OUT))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
        .leftJoin(GRADE)
        .on(GRADE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
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
}
