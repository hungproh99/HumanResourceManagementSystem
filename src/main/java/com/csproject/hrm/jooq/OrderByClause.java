package com.csproject.hrm.jooq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderByClause {
    public String field;
    public OrderBy orderBy;
}
