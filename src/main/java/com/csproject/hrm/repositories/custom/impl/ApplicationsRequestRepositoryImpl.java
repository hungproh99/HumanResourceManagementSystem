package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.ApplicationsRequestRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.aspectj.util.LangUtil.isEmpty;
import static org.jooq.codegen.maven.example.tables.ApplicationsRequest.APPLICATIONS_REQUEST;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.RequestStatus.REQUEST_STATUS;
import static org.jooq.codegen.maven.example.tables.RequestType.REQUEST_TYPE;
import static org.jooq.codegen.maven.example.tables.RequestName.REQUEST_NAME;
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
    field2Map.put(IS_BOOKMARK, APPLICATIONS_REQUEST.IS_BOOKMARK);
    field2Map.put(Constants.REQUEST_STATUS, APPLICATIONS_REQUEST.REQUEST_STATUS);
    field2Map.put(Constants.REQUEST_TYPE, APPLICATIONS_REQUEST.REQUEST_TYPE);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<ApplicationsRequestRespone> getListApplicationRequestByEmployeeId(
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
      orderByList.add(EMPLOYEE.EMPLOYEE_ID.desc());
    }

    for (OrderByClause clause : queryParam.orderByList) {

      final Field<?> field = field2Map.get(clause.field);

      if (Objects.isNull(field)) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
      }
      if (clause.field.equals(IS_BOOKMARK_PARAM)) {
        if (clause.orderBy.equals(OrderBy.ASC)) {
          orderByList.add(
              when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).asc().nullsLast());
        } else {
          orderByList.add(
              when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).desc().nullsLast());
        }
      }
      if (clause.orderBy.equals(OrderBy.ASC)) {
        orderByList.add(field.asc().nullsLast());
      } else {
        orderByList.add(field.desc().nullsLast());
      }
    }

    return getListApplicationRequest(conditions, orderByList, queryParam.pagination, employeeId)
        .fetchInto(ApplicationsRequestRespone.class);
  }

  private Select<?> getListApplicationRequest(
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
            (when(EMPLOYEE.WORKING_STATUS.isTrue(), "True"))
                .when(EMPLOYEE.WORKING_STATUS.isFalse(), "False")
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
        .offset(pagination.offset);
  }
}
