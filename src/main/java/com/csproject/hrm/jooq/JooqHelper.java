package com.csproject.hrm.jooq;

import com.csproject.hrm.common.enums.ComparisonOperator;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jwt.config.AuthEntryPointJwt;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.aspectj.util.LangUtil.isEmpty;

@Component
public class JooqHelper {

  private static final Logger Logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  public List<OrderField<?>> queryOrderBy(
      QueryParam queryParam, final Map<String, Field<?>> fieldMap, final Field<?> defaultField) {
    final List<OrderField<?>> orderByList = new ArrayList<>();

    if (null == queryParam || isEmpty(queryParam.orderByList)) {
      orderByList.add(defaultField.desc());
      return orderByList;
    }

    for (OrderByClause clause : queryParam.orderByList) {

      final Field<?> field = fieldMap.get(clause.field);

      if (Objects.isNull(field)) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
      }
      if (clause.orderBy.equals(OrderBy.ASC)) {
        orderByList.add(field.asc().nullsLast());
      } else {
        orderByList.add(field.desc().nullsLast());
      }
    }
    Logger.info("OrderBy conditions are: {}", orderByList);
    return orderByList;
  }

  public List<Condition> queryFilters(QueryParam queryParam, final Map<String, Field<?>> fieldMap) {

    if (queryParam == null || queryParam.filters == null) {
      return Collections.emptyList();
    }

    final List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = fieldMap.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }

            final var newCondition = condition(filter, field);
            condition = condition.and(newCondition);
          }
          conditions.add(condition);
        });

    Logger.info("Conditions for is: {}", conditions);
    return conditions;
  }

  private Condition condition(QueryFilter filter, Field<?> field) {

    if (StringUtils.length(filter.condition) < 3) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
    }

    final var subStr = filter.condition.substring(0, 3);
    final var operator = ComparisonOperator.of(subStr);
    final var value = filter.condition.substring(3);

    switch (operator) {
      case AEQ:
        return field.upper().eq(value.toUpperCase());
      case ALK:
        return field.upper().like(PERCENT_CHARACTER + value.toUpperCase() + PERCENT_CHARACTER);
      case ALT:
        return lt(field, value);
      case AGE:
        return ge(field, value);
      case AGT:
        return gt(field, value);
      case ALE:
        return le(field, value);
      case ANE:
        return ne(field, value);
      case AIN:
        return field.isNull();
      case ANN:
        return field.isNotNull();
      case ABT:
        return bt(field, value);
      case ANL:
        return field.upper().notLike(PERCENT_CHARACTER + value.toUpperCase() + PERCENT_CHARACTER);
      default:
        return DSL.noCondition();
    }
  }

  private Condition lt(Field<?> field, String value) {
    if (field.getDataType().isTimestamp()) {
      return field.cast(Timestamp.class).lt(new Timestamp(Long.valueOf(value)));
    } else if (field.getDataType().isNumeric()) {
      return field.cast(Long.class).lt(Long.valueOf((value)));
    }
    return DSL.noCondition();
  }

  private Condition ge(Field<?> field, String value) {
    if (field.getDataType().isTimestamp()) {
      return field.cast(Timestamp.class).ge(new Timestamp(Long.valueOf(value)));
    } else if (field.getDataType().isNumeric()) {
      return field.cast(Long.class).ge(Long.valueOf((value)));
    }
    return DSL.noCondition();
  }

  private Condition gt(Field<?> field, String value) {
    if (field.getDataType().isTimestamp()) {
      return field.cast(Timestamp.class).gt(new Timestamp(Long.valueOf(value)));
    } else if (field.getDataType().isNumeric()) {
      return field.cast(Long.class).gt(Long.valueOf((value)));
    }
    return DSL.noCondition();
  }

  private Condition le(Field<?> field, String value) {
    if (field.getDataType().isTimestamp()) {
      return field.cast(Timestamp.class).le(new Timestamp(Long.valueOf(value)));
    } else if (field.getDataType().isNumeric()) {
      return field.cast(Long.class).le(Long.valueOf((value)));
    }
    return DSL.noCondition();
  }

  private Condition ne(Field<?> field, String value) {
    if (field.getDataType().isTimestamp()) {
      return field.cast(Timestamp.class).ne(new Timestamp(Long.valueOf(value)));
    } else if (field.getDataType().isNumeric()) {
      return field.cast(Long.class).ne(Long.valueOf((value)));
    } else if (field.getDataType().isString()) {
      return field.upper().ne(value.toUpperCase());
    }
    return DSL.noCondition();
  }

  private Condition bt(Field<?> field, String value) {
    final var split = value.split(DASH_CHARACTER, TWO_NUMBER);
    final var less = split[0];
    final var greater = split[1];
    if (field.getDataType().isTimestamp()) {
      return field
          .cast(Timestamp.class)
          .between(new Timestamp(Long.valueOf(less)), new Timestamp(Long.valueOf(greater)));
    } else if (field.getDataType().isNumeric()) {
      return field.cast(Long.class).between(Long.valueOf(less), Long.valueOf(greater));
    }
    return DSL.noCondition();
  }
}