package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.TaxPolicyResponse;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.TaxPolicyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.jooq.codegen.maven.example.tables.TaxPolicy.TAX_POLICY;
import static org.jooq.codegen.maven.example.tables.TaxPolicyType.TAX_POLICY_TYPE;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class TaxPolicyRepositoryImpl implements TaxPolicyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(Constants.TAX_POLICY_TYPE, TAX_POLICY_TYPE.TAX_POLICY_TYPE_);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<TaxPolicyResponse> getListTaxPolicy(QueryParam queryParam) {
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, TAX_POLICY.TAX_POLICY_ID);
    return getListTaxPolicy(conditions, orderByList, queryParam.pagination)
        .fetchInto(TaxPolicyResponse.class);
  }

  private Select<?> getListTaxPolicy(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            TAX_POLICY.TAX_POLICY_ID,
            TAX_POLICY.TAX_POLICY_TYPE_ID,
            TAX_POLICY.CREATED_DATE,
            TAX_POLICY.EFFECTIVE_DATE,
            TAX_POLICY_TYPE.TAX_POLICY_TYPE_,
            TAX_POLICY.TAX_POLICY_NAME,
            TAX_POLICY.DESCRIPTION,
            TAX_POLICY.PERCENTAGE,
            (when(TAX_POLICY.TAX_POLICY_STATUS.isTrue(), "true")
                    .when(TAX_POLICY.TAX_POLICY_STATUS.isFalse(), "false"))
                .as(TAX_POLICY.TAX_POLICY_STATUS))
        .from(TAX_POLICY)
        .leftJoin(TAX_POLICY_TYPE)
        .on(TAX_POLICY.TAX_POLICY_TYPE_ID.eq(TAX_POLICY_TYPE.TAX_POLICY_TYPE_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public int getTotalTaxPolicy(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, TAX_POLICY.TAX_POLICY_ID);

    final var query =
        dslContext
            .select(TAX_POLICY.TAX_POLICY_ID)
            .from(TAX_POLICY)
            .leftJoin(TAX_POLICY_TYPE)
            .on(TAX_POLICY.TAX_POLICY_TYPE_ID.eq(TAX_POLICY_TYPE.TAX_POLICY_TYPE_ID))
            .where(conditions);
    return dslContext.fetchCount(query);
  }
}