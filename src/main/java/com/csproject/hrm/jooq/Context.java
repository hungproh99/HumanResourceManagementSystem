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

import static com.csproject.hrm.common.constant.Constants.*;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Context {
    public QueryParam queryParam(Map<String, String> allRequestParam) {
        final QueryParam param = new QueryParam();

        String paging = null;
        String filter = null;
        String orderBy = null;

        for (Map.Entry<String, String> entry : allRequestParam.entrySet()) {
            if (entry.getKey().equals(PAGING)) {
                paging = entry.getValue();
            } else if (entry.getKey().equals(FILTER)) {
                filter = entry.getValue();
            } else if (entry.getKey().equals(ORDER_BY)) {
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
            String[] splitComma = paging.split(COMMA);
            for (String data : splitComma) {
                String[] splitColon = data.split(COLON, TWO_NUMBER);
                if (isInvalidFilter(splitColon)) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, PAGING_INVALID);
                }
                if (splitColon[ZERO_NUMBER].equals(OFFSET)) {
                    offsetStr = splitColon[ONE_NUMBER];
                } else if (splitColon[ZERO_NUMBER].equals(LIMIT)) {
                    limitStr = splitColon[ONE_NUMBER];
                }
            }
        }
        try {
            int offset = Integer.parseInt(offsetStr);
            int limit = Integer.parseInt(limitStr);

            if (offset < ZERO_NUMBER) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, INVALID_OFFSET);
            }

            if (limit < ZERO_NUMBER) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, INVALID_LIMIT);
            }

            return new Pagination(offset * limit, limit);
        } catch (NumberFormatException e) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, INVALID_NUMBER_FORMAT);
        }
    }

    // filter1=name:abc,id:abc
    private List<QueryFilter> filters(String filter) {

        if (isBlank(filter)) {
            return new ArrayList<>();
        }

        return Arrays.stream(filter.split(COMMA)).map(each -> {
            final var split = each.split(COLON, TWO_NUMBER);
            if (isInvalidFilter(split)) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            return new QueryFilter(split[ZERO_NUMBER], URLDecoder.decode(split[ONE_NUMBER], StandardCharsets.UTF_8));
        }).collect(Collectors.toList());
    }

    private boolean isInvalidFilter(String[] split) {
        return split.length != TWO_NUMBER || isBlank(split[ZERO_NUMBER]) || isBlank(split[ONE_NUMBER]);
    }

    // orderby1:asc,orderBy2:desc
    private List<OrderByClause> orderBy(String orderBy) {

        if (isBlank(orderBy)) {
            return new ArrayList<>();
        }

        return Arrays.stream(orderBy.split(COMMA)).map(each -> {
            final var split = each.split(COLON);
            if (isInvalidFilter(split)) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
            }

            return new OrderByClause(split[ZERO_NUMBER], OrderBy.of(split[ONE_NUMBER]));
        }).collect(Collectors.toList());
    }
}
