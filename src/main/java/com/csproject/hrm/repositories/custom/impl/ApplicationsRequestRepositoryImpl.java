package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.ApplicationsRequestRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.aspectj.util.LangUtil.isEmpty;
import static org.jooq.codegen.maven.example.tables.ApplicationsRequest.APPLICATIONS_REQUEST;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Forwards.FORWARDS;
import static org.jooq.codegen.maven.example.tables.RequestName.REQUEST_NAME;
import static org.jooq.codegen.maven.example.tables.RequestStatus.REQUEST_STATUS;
import static org.jooq.codegen.maven.example.tables.RequestType.REQUEST_TYPE;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class ApplicationsRequestRepositoryImpl implements ApplicationsRequestRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
    field2Map.put(FULL_NAME, EMPLOYEE.FULL_NAME);
    field2Map.put(CREATE_DATE, APPLICATIONS_REQUEST.CREATE_DATE);
    field2Map.put(IS_BOOKMARK_PARAM, APPLICATIONS_REQUEST.IS_BOOKMARK);
    field2Map.put(REQUEST_STATUS_PARAM, REQUEST_STATUS.NAME);
    field2Map.put(REQUEST_TYPE_PARAM, REQUEST_TYPE.NAME);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<ApplicationsRequestRespone> getListApplicationRequestReceive(
      QueryParam queryParam, String employeeId) {
    final List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          Condition requestTypeCondition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(REQUEST_TYPE_PARAM)) {
              requestTypeCondition = requestTypeCondition.or(queryHelper.condition(filter, field));
            } else {
              condition = condition.and(queryHelper.condition(filter, field));
            }
          }
          condition = condition.and(requestTypeCondition);
          conditions.add(condition);
        });

    final List<OrderField<?>> orderByList = new ArrayList<>();

    if (null == queryParam || isEmpty(queryParam.orderByList)) {
      orderByList.add(APPLICATIONS_REQUEST.LATEST_DATE.desc());
    }

    for (OrderByClause clause : queryParam.orderByList) {

      final Field<?> field = field2Map.get(clause.field);

      if (Objects.isNull(field)) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
      }
      if (clause.field.equals(IS_BOOKMARK_PARAM)) {
        if (clause.orderBy.equals(OrderBy.ASC)) {
          orderByList.add(when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).asc());
        } else {
          orderByList.add(when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).desc());
        }
      } else {
        if (clause.orderBy.equals(OrderBy.ASC)) {
          orderByList.add(field.asc().nullsLast());
        } else {
          orderByList.add(field.desc().nullsLast());
        }
      }
    }

    return getListApplicationRequestReceive(
            conditions, orderByList, queryParam.pagination, employeeId)
        .fetchInto(ApplicationsRequestRespone.class);
  }

  @Override
  public List<ApplicationsRequestRespone> getListApplicationRequestSend(
      QueryParam queryParam, String employeeId) {
    final List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          Condition requestTypeCondition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(REQUEST_TYPE_PARAM)) {
              requestTypeCondition = requestTypeCondition.or(queryHelper.condition(filter, field));
            } else {
              condition = condition.and(queryHelper.condition(filter, field));
            }
          }
          condition = condition.and(requestTypeCondition);
          conditions.add(condition);
        });

    final List<OrderField<?>> orderByList = new ArrayList<>();

    if (null == queryParam || isEmpty(queryParam.orderByList)) {
      orderByList.add(APPLICATIONS_REQUEST.LATEST_DATE.desc());
    }

    for (OrderByClause clause : queryParam.orderByList) {

      final Field<?> field = field2Map.get(clause.field);

      if (Objects.isNull(field)) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
      }
      if (clause.field.equals(IS_BOOKMARK_PARAM)) {
        if (clause.orderBy.equals(OrderBy.ASC)) {
          orderByList.add(when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).asc());
        } else {
          orderByList.add(when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).desc());
        }
      } else {
        if (clause.orderBy.equals(OrderBy.ASC)) {
          orderByList.add(field.asc().nullsLast());
        } else {
          orderByList.add(field.desc().nullsLast());
        }
      }
    }

    return getListApplicationRequestSend(conditions, orderByList, queryParam.pagination, employeeId)
        .fetchInto(ApplicationsRequestRespone.class);
  }

  private Select<?> getListApplicationRequestReceive(
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination,
      String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    TableLike<?> selectForward =
            dslContext.select(FORWARDS.APPLICATIONS_REQUEST_ID, FORWARDS.EMPLOYEE_ID).from(FORWARDS);

    return dslContext
        .select(
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            APPLICATIONS_REQUEST.CREATE_DATE,
            concat(REQUEST_NAME.NAME).concat(" ").concat(REQUEST_TYPE.NAME).as(REQUEST_TITLE),
            APPLICATIONS_REQUEST.DESCRIPTION,
            REQUEST_STATUS.NAME.as(Constants.REQUEST_STATUS),
            APPLICATIONS_REQUEST.LATEST_DATE.as(CHANGE_STATUS_TIME),
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.APPROVER,
            (when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), "True")
                    .when(APPLICATIONS_REQUEST.IS_BOOKMARK.isFalse(), "False"))
                .as(IS_BOOKMARK))
        .from(EMPLOYEE)
        .leftJoin(APPLICATIONS_REQUEST)
        .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(REQUEST_STATUS)
        .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
        .leftJoin(REQUEST_NAME)
        .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.TYPE_ID))
        .leftJoin(REQUEST_TYPE)
        .on(APPLICATIONS_REQUEST.REQUEST_TYPE.eq(REQUEST_TYPE.TYPE_ID))
        .where(conditions)
        .and(APPLICATIONS_REQUEST.APPROVER.eq(employeeId))
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset)
            .unionAll(dslContext
                    .select(
                            EMPLOYEE.EMPLOYEE_ID,
                            EMPLOYEE.FULL_NAME,
                            APPLICATIONS_REQUEST.CREATE_DATE,
                            concat(REQUEST_NAME.NAME).concat(" ").concat(REQUEST_TYPE.NAME).as(REQUEST_TITLE),
                            APPLICATIONS_REQUEST.DESCRIPTION,
                            REQUEST_STATUS.NAME.as(Constants.REQUEST_STATUS),
                            APPLICATIONS_REQUEST.LATEST_DATE.as(CHANGE_STATUS_TIME),
                            APPLICATIONS_REQUEST.DURATION,
                            APPLICATIONS_REQUEST.APPROVER,
                            (when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), "True")
                                    .when(APPLICATIONS_REQUEST.IS_BOOKMARK.isFalse(), "False"))
                                    .as(IS_BOOKMARK))
                    .from(EMPLOYEE)
                    .leftJoin(APPLICATIONS_REQUEST)
                    .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
                    .leftJoin(REQUEST_STATUS)
                    .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
                    .leftJoin(REQUEST_NAME)
                    .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.TYPE_ID))
                    .leftJoin(REQUEST_TYPE)
                    .on(APPLICATIONS_REQUEST.REQUEST_TYPE.eq(REQUEST_TYPE.TYPE_ID))
                    .leftJoin(selectForward)
                    .on(
                            selectForward
                                    .field(FORWARDS.APPLICATIONS_REQUEST_ID)
                                    .eq(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID))
                    .where(conditions)
                    .and(selectForward.field(FORWARDS.EMPLOYEE_ID).eq(employeeId))
                    .orderBy(sortFields)
                    .limit(pagination.limit)
                    .offset(pagination.offset));
  }

  private Select<?> getListApplicationRequestSend(
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination,
      String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            APPLICATIONS_REQUEST.CREATE_DATE,
            concat(REQUEST_NAME.NAME).concat(" ").concat(REQUEST_TYPE.NAME).as(REQUEST_TITLE),
            APPLICATIONS_REQUEST.DESCRIPTION,
            REQUEST_STATUS.NAME.as(Constants.REQUEST_STATUS),
            APPLICATIONS_REQUEST.LATEST_DATE.as(CHANGE_STATUS_TIME),
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.APPROVER,
            (when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), "True")
                    .when(APPLICATIONS_REQUEST.IS_BOOKMARK.isFalse(), "False"))
                .as(IS_BOOKMARK))
        .from(EMPLOYEE)
        .leftJoin(APPLICATIONS_REQUEST)
        .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(REQUEST_STATUS)
        .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
        .leftJoin(REQUEST_NAME)
        .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.TYPE_ID))
        .leftJoin(REQUEST_TYPE)
        .on(APPLICATIONS_REQUEST.REQUEST_TYPE.eq(REQUEST_TYPE.TYPE_ID))
        .where(conditions)
        .and(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(employeeId))
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .insertInto(
            APPLICATIONS_REQUEST,
            APPLICATIONS_REQUEST.EMPLOYEE_ID,
            APPLICATIONS_REQUEST.REQUEST_TYPE,
            APPLICATIONS_REQUEST.REQUEST_STATUS,
            APPLICATIONS_REQUEST.REQUEST_NAME,
            APPLICATIONS_REQUEST.CREATE_DATE,
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.LATEST_DATE,
            APPLICATIONS_REQUEST.DESCRIPTION,
            APPLICATIONS_REQUEST.APPROVER,
            APPLICATIONS_REQUEST.IS_BOOKMARK,
            APPLICATIONS_REQUEST.IS_REMIND)
        .values(
            applicationsRequest.getEmployee_id(),
            applicationsRequest.getRequest_type_id(),
            Long.valueOf("1"),
            applicationsRequest.getRequest_name_id(),
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7),
            LocalDateTime.now(),
            applicationsRequest.getDescription(),
            applicationsRequest.getApprover(),
            Boolean.FALSE,
            Boolean.FALSE)
        .execute();
  }
}