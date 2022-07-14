package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.dto.PolicyDto;
import com.csproject.hrm.dto.response.PolicyCategoryResponse;
import com.csproject.hrm.dto.response.PolicyResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.jooq.Pagination;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.custom.PolicyRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class PolicyRepositoryImpl implements PolicyRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(Constants.CATEGORY_ID, POLICY_CATEGORY.POLICY_CATEGORY_ID);
    field2Map.put(Constants.POLICY_ID, POLICY.POLICY_ID);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<PolicyResponse> getListPolicyByCategoryID(QueryParam queryParam) {
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final List<OrderField<?>> orderByList =
        queryHelper.queryOrderBy(queryParam, field2Map, POLICY.POLICY_ID);
    return getListPolicyByCategoryID(conditions, orderByList, queryParam.pagination)
        .fetchInto(PolicyResponse.class);
  }

  private Select<?> getListPolicyByCategoryID(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            POLICY.POLICY_ID,
            POLICY.POLICY_TYPE_ID,
            POLICY.CREATED_DATE,
            POLICY.EFFECTIVE_DATE,
            POLICY_TYPE.POLICY_TYPE_,
            POLICY_TYPE.POLICY_NAME,
            POLICY.DESCRIPTION,
            POLICY_CATEGORY.POLICY_CATEGORY_,
            (when(POLICY.POLICY_STATUS.isTrue(), "true")
                    .when(POLICY.POLICY_STATUS.isFalse(), "false"))
                .as(POLICY.POLICY_STATUS))
        .from(POLICY)
        .leftJoin(POLICY_TYPE)
        .on(POLICY.POLICY_TYPE_ID.eq(POLICY_TYPE.POLICY_TYPE_ID))
        .leftJoin(POLICY_CATEGORY)
        .on(POLICY_CATEGORY.POLICY_CATEGORY_ID.eq(POLICY_TYPE.POLICY_CATEGORY_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public int getTotalPolicyByCategoryID(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);

    final var query =
        dslContext
            .select(POLICY.POLICY_ID)
            .from(POLICY)
            .leftJoin(POLICY_TYPE)
            .on(POLICY.POLICY_TYPE_ID.eq(POLICY_TYPE.POLICY_TYPE_ID))
            .leftJoin(POLICY_CATEGORY)
            .on(POLICY_CATEGORY.POLICY_CATEGORY_ID.eq(POLICY_TYPE.POLICY_CATEGORY_ID))
            .where(conditions);
    return dslContext.fetchCount(query);
  }

  @Override
  public List<PolicyCategoryResponse> getAllPolicyCategory() {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(POLICY_CATEGORY.POLICY_CATEGORY_ID, POLICY_CATEGORY.POLICY_CATEGORY_)
        .from(POLICY_CATEGORY)
        .fetchInto(PolicyCategoryResponse.class);
  }

  @Override
  public Long getTaxPolicyTypeIDByTaxName(String taxName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(POLICY_TYPE.POLICY_TYPE_ID)
        .from(POLICY_TYPE)
        .where(POLICY_TYPE.POLICY_TYPE_.eq(taxName))
        .fetchOneInto(Long.class);
  }

  @Override
  public Optional<PolicyDto> getPolicyDtoByPolicyType(String policyType) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(POLICY_TYPE.POLICY_NAME, POLICY.DATA)
        .from(POLICY)
        .leftJoin(POLICY_TYPE)
        .on(POLICY_TYPE.POLICY_TYPE_ID.eq(POLICY.POLICY_TYPE_ID))
        .where(POLICY_TYPE.POLICY_TYPE_.eq(policyType))
        .fetchOptionalInto(PolicyDto.class);
  }
}