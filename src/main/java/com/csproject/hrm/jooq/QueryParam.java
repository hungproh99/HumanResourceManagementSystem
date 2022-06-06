package com.csproject.hrm.jooq;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryParam {
  public Pagination pagination;

  public List<QueryFilter> filters;

  public List<OrderByClause> orderByList;

  public static QueryParam defaultParam() {

    final var param = new QueryParam();

    param.setPagination(Pagination.defaultPage());
    param.setFilters(Collections.EMPTY_LIST);
    param.setOrderByList(Collections.EMPTY_LIST);

    return param;
  }
}