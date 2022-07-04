package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.WorkingPolicyResponse;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.WorkingPolicyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.jooq.codegen.maven.example.tables.WorkingPolicy.WORKING_POLICY;
import static org.jooq.codegen.maven.example.tables.WorkingPolicyType.WORKING_POLICY_TYPE;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class WorkingPolicyRepositoryImpl implements WorkingPolicyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(Constants.WORKING_POLICY_TYPE, WORKING_POLICY_TYPE.WORKING_POLICY_TYPE_);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<WorkingPolicyResponse> getListWorkingPolicy(QueryParam queryParam) {
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, WORKING_POLICY.WORKING_POLICY_ID);
    return getListWorkingPolicy(conditions, orderByList, queryParam.pagination)
        .fetchInto(WorkingPolicyResponse.class);
  }

  private Select<?> getListWorkingPolicy(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            WORKING_POLICY.WORKING_POLICY_ID,
            WORKING_POLICY.WORKING_POLICY_TYPE_ID,
            WORKING_POLICY.CREATED_DATE,
            WORKING_POLICY.EFFECTIVE_DATE,
            WORKING_POLICY_TYPE.WORKING_POLICY_TYPE_,
            WORKING_POLICY.WORKING_POLICY_NAME,
            WORKING_POLICY.DESCRIPTION,
            (when(WORKING_POLICY.WORKING_POLICY_STATUS.isTrue(), "true")
                    .when(WORKING_POLICY.WORKING_POLICY_STATUS.isFalse(), "false"))
                .as(WORKING_POLICY.WORKING_POLICY_STATUS))
        .from(WORKING_POLICY)
        .leftJoin(WORKING_POLICY_TYPE)
        .on(WORKING_POLICY.WORKING_POLICY_TYPE_ID.eq(WORKING_POLICY_TYPE.WORKING_POLICY_TYPE_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public int getTotalWorkingPolicy(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, WORKING_POLICY.WORKING_POLICY_ID);

    final var query =
        dslContext
            .select(WORKING_POLICY.WORKING_POLICY_ID)
            .from(WORKING_POLICY)
            .leftJoin(WORKING_POLICY_TYPE)
            .on(
                WORKING_POLICY.WORKING_POLICY_TYPE_ID.eq(
                    WORKING_POLICY_TYPE.WORKING_POLICY_TYPE_ID))
            .where(conditions);
    return dslContext.fetchCount(query);
  }
}