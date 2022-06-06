package com.csproject.hrm.jooq;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderByClause {
  public String field;
  public OrderBy orderBy;
}