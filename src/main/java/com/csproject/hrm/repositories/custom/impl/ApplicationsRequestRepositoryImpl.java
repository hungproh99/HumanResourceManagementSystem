package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.entities.ApplicationsRequest;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.jooq.Pagination;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.custom.ApplicationsRequestRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.tables.ApplicationsRequest.APPLICATIONS_REQUEST;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.impl.DSL.concat;

@AllArgsConstructor
public class ApplicationsRequestRepositoryImpl implements ApplicationsRequestRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
    field2Map.put(FULL_NAME, EMPLOYEE.FULL_NAME);
    field2Map.put(CREATE_DATE, APPLICATIONS_REQUEST.CREATE_DATE);
    field2Map.put(IS_BOOKMARK, APPLICATIONS_REQUEST.IS_BOOKMARK);
    field2Map.put(REQUEST_STATUS, APPLICATIONS_REQUEST.REQUEST_STATUS);
    field2Map.put(REQUEST_TYPE, APPLICATIONS_REQUEST.REQUEST_TYPE);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<ApplicationsRequest> getListApplicationRequest(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    return null;
  }

  private Select<?> getListApplicationRequest(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE.FULL_NAME,
            concat(APPLICATIONS_REQUEST.REQUEST_NAME)
                .concat(" ")
                .concat(APPLICATIONS_REQUEST.REQUEST_TYPE)
                .as(REQUEST_TITLE),
            APPLICATIONS_REQUEST.REQUEST_STATUS,
            APPLICATIONS_REQUEST.CREATE_DATE,
            APPLICATIONS_REQUEST.IS_BOOKMARK)
        .from(EMPLOYEE)
        .leftJoin(APPLICATIONS_REQUEST)
        .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }
}
