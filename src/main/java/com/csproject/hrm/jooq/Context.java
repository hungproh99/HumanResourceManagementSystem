package com.csproject.hrm.jooq;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Context {
    public QueryParam queryParam(Map<String, String> allRequestParam) {
        final QueryParam param = new QueryParam();

        String paging = null;
        String filter = null;
        String orderBy = null;

        for (Map.Entry<String, String> entry : allRequestParam.entrySet()) {
            if (entry.getKey().equals("paging")) {
                paging = entry.getValue();
            } else if (entry.getKey().equals("filter")) {
                filter = entry.getValue();
            } else if (entry.getKey().equals("orderBy")) {
                orderBy = entry.getValue();
            }
        }

        param.setPagination(pagination(paging));

        param.setFilters(filters(filter));

        param.setOrderByList(orderBy(orderBy));

        return param;
    }

    // paging=offset:1,limit:10
    private Pagination pagination(String paging) {
        String offsetStr = valueOf(Pagination.DEFAULT_OFFSET);
        String limitStr = valueOf(Pagination.DEFAULT_LIMIT);
        if (!isBlank(paging)) {
            String[] splitComma = paging.split(",");
            for (String data : splitComma) {
                String[] splitColon = data.split(":", 2);
                if (isInvalidFilter(splitColon)) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Paging");
                }
                if (splitColon[0].equals("offset")) {
                    offsetStr = splitColon[1];
                } else if (splitColon[0].equals("limit")) {
                    limitStr = splitColon[1];
                }
            }
        }
        try {
            int offset = Integer.parseInt(offsetStr);
            int limit = Integer.parseInt(limitStr);

            return new Pagination(offset * limit, limit);
        } catch (NumberFormatException e) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Number Format");
        }
    }

    // filter1=name:abc,id:abc
    private List<QueryFilter> filters(String filter) {

        if (isBlank(filter)) {
            return new ArrayList<>();
        }

        return Arrays.stream(filter.split(",")).map(each -> {
            final var split = each.split(":", 2);
            if (isInvalidFilter(split)) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Filter");
            }
            return new QueryFilter(split[0], URLDecoder.decode(split[1], StandardCharsets.UTF_8));
        }).collect(Collectors.toList());
    }

    private boolean isInvalidFilter(String[] split) {
        return split.length != 2 || isBlank(split[0]) || isBlank(split[1]);
    }

    // orderby1:asc,orderBy2:desc
    private List<OrderByClause> orderBy(String orderBy) {

        if (isBlank(orderBy)) {
            return new ArrayList<>();
        }

        return Arrays.stream(orderBy.split(",")).map(each -> {
            final var split = each.split(":");
            if (isInvalidFilter(split)) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid OrderBy");
            }

            return new OrderByClause(split[0], OrderBy.of(split[1]));
        }).collect(Collectors.toList());
    }
}
