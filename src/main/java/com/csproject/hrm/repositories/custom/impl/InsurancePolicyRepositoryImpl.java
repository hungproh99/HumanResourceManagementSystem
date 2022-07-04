package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.InsurancePolicyResponse;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.InsurancePolicyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.jooq.codegen.maven.example.tables.InsurancePolicy.INSURANCE_POLICY;
import static org.jooq.codegen.maven.example.tables.InsurancePolicyType.INSURANCE_POLICY_TYPE;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class InsurancePolicyRepositoryImpl implements InsurancePolicyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(Constants.INSURANCE_POLICY_TYPE, INSURANCE_POLICY_TYPE.INSURANCE_POLICY_TYPE_);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<InsurancePolicyResponse> getListInsurancePolicy(QueryParam queryParam) {
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, INSURANCE_POLICY.INSURANCE_POLICY_ID);
    return getListInsurancePolicy(conditions, orderByList, queryParam.pagination)
        .fetchInto(InsurancePolicyResponse.class);
  }

  private Select<?> getListInsurancePolicy(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            INSURANCE_POLICY.INSURANCE_POLICY_ID,
            INSURANCE_POLICY.INSURANCE_POLICY_TYPE_ID,
            INSURANCE_POLICY.CREATED_DATE,
            INSURANCE_POLICY.EFFECTIVE_DATE,
            INSURANCE_POLICY_TYPE.INSURANCE_POLICY_TYPE_,
            INSURANCE_POLICY.INSURANCE_POLICY_NAME,
            INSURANCE_POLICY.DESCRIPTION,
            (when(INSURANCE_POLICY.INSURANCE_POLICY_STATUS.isTrue(), "true")
                    .when(INSURANCE_POLICY.INSURANCE_POLICY_STATUS.isFalse(), "false"))
                .as(INSURANCE_POLICY.INSURANCE_POLICY_STATUS))
        .from(INSURANCE_POLICY)
        .leftJoin(INSURANCE_POLICY_TYPE)
        .on(
            INSURANCE_POLICY.INSURANCE_POLICY_TYPE_ID.eq(
                INSURANCE_POLICY_TYPE.INSURANCE_POLICY_TYPE_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public int getTotalInsurancePolicy(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, INSURANCE_POLICY.INSURANCE_POLICY_ID);

    final var query =
        dslContext
            .select(INSURANCE_POLICY.INSURANCE_POLICY_ID)
            .from(INSURANCE_POLICY)
            .leftJoin(INSURANCE_POLICY_TYPE)
            .on(
                INSURANCE_POLICY.INSURANCE_POLICY_TYPE_ID.eq(
                    INSURANCE_POLICY_TYPE.INSURANCE_POLICY_TYPE_ID))
            .where(conditions);
    return dslContext.fetchCount(query);
  }
}