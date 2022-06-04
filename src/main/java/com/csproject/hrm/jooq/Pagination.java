package com.csproject.hrm.jooq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 10;

    public int offset;

    public int limit;

    public static Pagination defaultPage() {
        return new Pagination(DEFAULT_OFFSET, DEFAULT_LIMIT);
    }
}
