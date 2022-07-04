package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.CAndBPolicyResponse;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.CAndBPolicyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.jooq.codegen.maven.example.Tables.COMPENSATION_BENEFIT_POLICY;
import static org.jooq.codegen.maven.example.Tables.COMPENSATION_BENEFIT_POLICY_TYPE;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class CAndBPolicyRepositoryImpl implements CAndBPolicyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(
        Constants.C_AND_B_POLICY_TYPE,
        COMPENSATION_BENEFIT_POLICY_TYPE.COMPENSATION_BENEFIT_POLICY_TYPE_);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<CAndBPolicyResponse> getListCAndBPolicy(QueryParam queryParam) {
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(
            queryParam, field2Map, COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_ID);
    return getListCAndBPolicy(conditions, orderByList, queryParam.pagination)
        .fetchInto(CAndBPolicyResponse.class);
  }

  private Select<?> getListCAndBPolicy(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_ID,
            COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_POLICY_TYPE_ID,
            COMPENSATION_BENEFIT_POLICY.CREATED_DATE,
            COMPENSATION_BENEFIT_POLICY.EFFECTIVE_DATE,
            COMPENSATION_BENEFIT_POLICY_TYPE.COMPENSATION_BENEFIT_POLICY_TYPE_,
            COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_POLICY_NAME,
            COMPENSATION_BENEFIT_POLICY.DESCRIPTION,
            (when(COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_STATUS.isTrue(), "true")
                    .when(
                        COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_STATUS.isFalse(), "false"))
                .as(COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_STATUS))
        .from(COMPENSATION_BENEFIT_POLICY)
        .leftJoin(COMPENSATION_BENEFIT_POLICY_TYPE)
        .on(
            COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_POLICY_TYPE_ID.eq(
                COMPENSATION_BENEFIT_POLICY_TYPE.COMPENSATION_BENEFIT_POLICY_TYPE_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public int getTotalCAndBPolicy(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(
            queryParam, field2Map, COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_ID);

    final var query =
        dslContext
            .select(COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_ID)
            .from(COMPENSATION_BENEFIT_POLICY)
            .leftJoin(COMPENSATION_BENEFIT_POLICY_TYPE)
            .on(
                COMPENSATION_BENEFIT_POLICY.COMPENSATION_BENEFIT_POLICY_TYPE_ID.eq(
                    COMPENSATION_BENEFIT_POLICY_TYPE.COMPENSATION_BENEFIT_POLICY_TYPE_ID))
            .where(conditions);
    return dslContext.fetchCount(query);
  }
}