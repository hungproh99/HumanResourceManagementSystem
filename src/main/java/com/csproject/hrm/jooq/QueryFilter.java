package com.csproject.hrm.jooq;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryFilter {
  public String field;
  public String condition;
}