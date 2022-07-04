package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.LeavePolicyResponse;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.LeavePolicyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.jooq.codegen.maven.example.Tables.LEAVE_POLICY;
import static org.jooq.codegen.maven.example.Tables.LEAVE_POLICY_TYPE;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class LeavePolicyRepositoryImpl implements LeavePolicyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(Constants.LEAVE_POLICY_TYPE, LEAVE_POLICY_TYPE.LEAVE_POLICY_TYPE_);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<LeavePolicyResponse> getListLeavePolicy(QueryParam queryParam) {
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, LEAVE_POLICY.LEAVE_POLICY_ID);
    return getListLeavePolicy(conditions, orderByList, queryParam.pagination)
        .fetchInto(LeavePolicyResponse.class);
  }

  private Select<?> getListLeavePolicy(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            LEAVE_POLICY.LEAVE_POLICY_ID,
            LEAVE_POLICY.LEAVE_POLICY_TYPE_ID,
            LEAVE_POLICY.CREATED_DATE,
            LEAVE_POLICY.EFFECTIVE_DATE,
            LEAVE_POLICY_TYPE.LEAVE_POLICY_TYPE_,
            LEAVE_POLICY.LEAVE_POLICY_NAME,
            LEAVE_POLICY.DESCRIPTION,
            (when(LEAVE_POLICY.LEAVE_POLICY_STATUS.isTrue(), "true")
                    .when(LEAVE_POLICY.LEAVE_POLICY_STATUS.isFalse(), "false"))
                .as(LEAVE_POLICY.LEAVE_POLICY_STATUS))
        .from(LEAVE_POLICY)
        .leftJoin(LEAVE_POLICY_TYPE)
        .on(LEAVE_POLICY.LEAVE_POLICY_TYPE_ID.eq(LEAVE_POLICY_TYPE.LEAVE_POLICY_TYPE_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public int getTotalLeavePolicy(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, LEAVE_POLICY.LEAVE_POLICY_ID);

    final var query =
        dslContext
            .select(LEAVE_POLICY.LEAVE_POLICY_ID)
            .from(LEAVE_POLICY)
            .leftJoin(LEAVE_POLICY_TYPE)
            .on(LEAVE_POLICY.LEAVE_POLICY_TYPE_ID.eq(LEAVE_POLICY_TYPE.LEAVE_POLICY_TYPE_ID))
            .where(conditions);
    return dslContext.fetchCount(query);
  }
}