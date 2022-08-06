package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.common.enums.ERequestName;
import com.csproject.hrm.common.enums.ERequestStatus;
import com.csproject.hrm.common.enums.ERequestType;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.ApplicationsRequestCreateRequest;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.RejectApplicationRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ApplicationRequestRemindResponse;
import com.csproject.hrm.dto.response.ApplicationsRequestResponse;
import com.csproject.hrm.dto.response.PolicyTypeAndNameResponse;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.ApplicationsRequestRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.aspectj.util.LangUtil.isEmpty;
import static org.jooq.codegen.maven.example.Tables.POLICY_TYPE;
import static org.jooq.codegen.maven.example.Tables.REVIEW_REQUEST;
import static org.jooq.codegen.maven.example.tables.ApplicationsRequest.APPLICATIONS_REQUEST;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Policy.POLICY;
import static org.jooq.codegen.maven.example.tables.RequestName.REQUEST_NAME;
import static org.jooq.codegen.maven.example.tables.RequestStatus.REQUEST_STATUS;
import static org.jooq.codegen.maven.example.tables.RequestType.REQUEST_TYPE;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.when;

@AllArgsConstructor
public class ApplicationsRequestRepositoryImpl implements ApplicationsRequestRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
    field2Map.put(FULL_NAME, EMPLOYEE.FULL_NAME);
    field2Map.put(CREATE_DATE, APPLICATIONS_REQUEST.CREATE_DATE);
    field2Map.put(IS_BOOKMARK_PARAM, APPLICATIONS_REQUEST.IS_BOOKMARK);
    field2Map.put(REQUEST_STATUS_PARAM, REQUEST_STATUS.NAME);
    field2Map.put(REQUEST_TYPE_PARAM, REQUEST_TYPE.NAME);
    field2Map.put(REQUEST_ID_PARAM, APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<ApplicationsRequestResponse> getListApplicationRequestReceive(
      QueryParam queryParam, String employeeId) {
    final List<Condition> conditions = getListConditionApplicationRequest(queryParam);

    final List<OrderField<?>> orderByList = getOrderFieldApplicationRequest(queryParam);

    List<ApplicationsRequestResponse> applicationsRequestResponseForwardList =
        getListApplicationRequestReceiveForward(
                conditions, orderByList, queryParam.pagination, employeeId)
            .fetchInto(ApplicationsRequestResponse.class);

    List<ApplicationsRequestResponse> applicationsRequestResponseList =
        Stream.of(
                applicationsRequestResponseForwardList,
                getListApplicationRequestReceive(
                        conditions, orderByList, queryParam.pagination, employeeId)
                    .fetchInto(ApplicationsRequestResponse.class))
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());

    applicationsRequestResponseList.forEach(
        applicationsRequestResponse -> {
          applicationsRequestResponse.setChecked_by(
              getListForwarderByRequestId(applicationsRequestResponse.getApplication_request_id())
                  .fetchInto(String.class));
          applicationsRequestResponse.setIs_enough_level(
              checkLevelOfManagerByRequestId(
                  employeeId, applicationsRequestResponse.getApplication_request_id()));
          applicationsRequestResponse.setRequest_name(
              ERequestName.getLabel(applicationsRequestResponse.getRequest_name()));
          applicationsRequestResponse.setRequest_type(
              ERequestType.getLabel(applicationsRequestResponse.getRequest_type()));
          applicationsRequestResponse.setType(
              checkNotificationOrRequest(
                  employeeId, applicationsRequestResponse.getApplication_request_id()));
        });

    return applicationsRequestResponseList;
  }

  @Override
  public List<ApplicationsRequestResponse> getListApplicationRequestSend(
      QueryParam queryParam, String employeeId) {
    final List<Condition> conditions = getListConditionApplicationRequest(queryParam);

    final List<OrderField<?>> orderByList = getOrderFieldApplicationRequest(queryParam);

    List<ApplicationsRequestResponse> applicationsRequestResponseList =
        getListApplicationRequestSend(conditions, orderByList, queryParam.pagination, employeeId)
            .fetchInto(ApplicationsRequestResponse.class);

    applicationsRequestResponseList.forEach(
        applicationsRequestResponse -> {
          applicationsRequestResponse.setChecked_by(
              getListForwarderByRequestId(applicationsRequestResponse.getApplication_request_id())
                  .fetchInto(String.class));
          applicationsRequestResponse.setRequest_name(
              ERequestName.getLabel(applicationsRequestResponse.getRequest_name()));
          applicationsRequestResponse.setRequest_type(
              ERequestType.getLabel(applicationsRequestResponse.getRequest_type()));
          applicationsRequestResponse.setType(
              checkNotificationOrRequest(
                  employeeId, applicationsRequestResponse.getApplication_request_id()));
        });

    return applicationsRequestResponseList;
  }

  private Select<?> getListApplicationRequestReceive(
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination,
      String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID,
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            APPLICATIONS_REQUEST.CREATE_DATE,
            REQUEST_NAME.NAME.as("request_name"),
            REQUEST_TYPE.NAME.as("request_type"),
            APPLICATIONS_REQUEST.DESCRIPTION,
            REQUEST_STATUS.NAME.as(Constants.REQUEST_STATUS),
            APPLICATIONS_REQUEST.LATEST_DATE.as(CHANGE_STATUS_TIME),
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.APPROVER,
            (when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), "True")
                    .when(APPLICATIONS_REQUEST.IS_BOOKMARK.isFalse(), "False"))
                .as(IS_BOOKMARK),
            APPLICATIONS_REQUEST.COMMENT)
        .from(EMPLOYEE)
        .leftJoin(APPLICATIONS_REQUEST)
        .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(REQUEST_STATUS)
        .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
        .leftJoin(REQUEST_NAME)
        .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.REQUEST_NAME_ID))
        .leftJoin(REQUEST_TYPE)
        .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
        .where(conditions)
        .and(APPLICATIONS_REQUEST.APPROVER.eq(employeeId))
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  private Select<?> getListApplicationRequestReceiveForward(
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination,
      String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    TableLike<?> selectReview =
        dslContext
            .select(REVIEW_REQUEST.APPLICATIONS_REQUEST_ID, REVIEW_REQUEST.EMPLOYEE_ID)
            .from(REVIEW_REQUEST);

    return dslContext
        .select(
            APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID,
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            APPLICATIONS_REQUEST.CREATE_DATE,
            REQUEST_NAME.NAME.as("request_name"),
            REQUEST_TYPE.NAME.as("request_type"),
            APPLICATIONS_REQUEST.DESCRIPTION,
            REQUEST_STATUS.NAME.as(Constants.REQUEST_STATUS),
            APPLICATIONS_REQUEST.LATEST_DATE.as(CHANGE_STATUS_TIME),
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.APPROVER,
            (when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), "True")
                    .when(APPLICATIONS_REQUEST.IS_BOOKMARK.isFalse(), "False"))
                .as(IS_BOOKMARK),
            APPLICATIONS_REQUEST.COMMENT)
        .from(EMPLOYEE)
        .leftJoin(APPLICATIONS_REQUEST)
        .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(REQUEST_STATUS)
        .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
        .leftJoin(REQUEST_NAME)
        .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.REQUEST_NAME_ID))
        .leftJoin(REQUEST_TYPE)
        .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
        .leftJoin(selectReview)
        .on(
            selectReview
                .field(REVIEW_REQUEST.APPLICATIONS_REQUEST_ID)
                .eq(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID))
        .where(conditions)
        .and(selectReview.field(REVIEW_REQUEST.EMPLOYEE_ID).eq(employeeId))
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  private Select<?> getListApplicationRequestSend(
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination,
      String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(
            APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID,
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            APPLICATIONS_REQUEST.CREATE_DATE,
            REQUEST_NAME.NAME.as("request_name"),
            REQUEST_TYPE.NAME.as("request_type"),
            APPLICATIONS_REQUEST.DESCRIPTION,
            REQUEST_STATUS.NAME.as(Constants.REQUEST_STATUS),
            APPLICATIONS_REQUEST.LATEST_DATE.as(CHANGE_STATUS_TIME),
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.APPROVER,
            (when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), "True")
                    .when(APPLICATIONS_REQUEST.IS_BOOKMARK.isFalse(), "False"))
                .as(IS_BOOKMARK),
            APPLICATIONS_REQUEST.COMMENT)
        .from(EMPLOYEE)
        .leftJoin(APPLICATIONS_REQUEST)
        .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(REQUEST_STATUS)
        .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
        .leftJoin(REQUEST_NAME)
        .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.REQUEST_NAME_ID))
        .leftJoin(REQUEST_TYPE)
        .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
        .where(conditions)
        .and(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(employeeId))
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  @Override
  public void insertApplicationRequest(
      ApplicationsRequestRequest applicationsRequest,
      LocalDateTime createdDate,
      LocalDateTime latestDate,
      LocalDateTime duration) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .insertInto(
            APPLICATIONS_REQUEST,
            APPLICATIONS_REQUEST.EMPLOYEE_ID,
            APPLICATIONS_REQUEST.REQUEST_STATUS,
            APPLICATIONS_REQUEST.REQUEST_NAME,
            APPLICATIONS_REQUEST.CREATE_DATE,
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.LATEST_DATE,
            APPLICATIONS_REQUEST.DESCRIPTION,
            APPLICATIONS_REQUEST.APPROVER,
            APPLICATIONS_REQUEST.IS_BOOKMARK)
        .values(
            applicationsRequest.getEmployeeId(),
            applicationsRequest.getRequestStatusId(),
            applicationsRequest.getRequestNameId(),
            createdDate,
            duration,
            latestDate,
            applicationsRequest.getDescription(),
            applicationsRequest.getApprover(),
            applicationsRequest.getIsBookmark())
        .execute();
  }

  @Override
  public void updateCheckedApplicationRequest(
      UpdateApplicationRequestRequest updateApplicationRequestRequest,
      String employeeId,
      boolean isRemind,
      LocalDateTime latestDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          DSL.using(configuration)
              .update(APPLICATIONS_REQUEST)
              .set(
                  APPLICATIONS_REQUEST.REQUEST_STATUS,
                  ERequestStatus.getValue(updateApplicationRequestRequest.getRequestStatus()))
              .set(APPLICATIONS_REQUEST.LATEST_DATE, latestDate)
              .set(APPLICATIONS_REQUEST.APPROVER, updateApplicationRequestRequest.getApproverId())
              .set(APPLICATIONS_REQUEST.IS_REMIND, isRemind)
              .where(
                  APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(
                      updateApplicationRequestRequest.getApplicationRequestId()))
              .execute();
          final var insertForwarder =
              dslContext
                  .insertInto(
                      REVIEW_REQUEST,
                      REVIEW_REQUEST.EMPLOYEE_ID,
                      REVIEW_REQUEST.APPLICATIONS_REQUEST_ID)
                  .values(employeeId, updateApplicationRequestRequest.getApplicationRequestId())
                  .execute();
        });
  }

  @Override
  public void updateRejectApplicationRequest(
      RejectApplicationRequestRequest rejectApplicationRequestRequest, LocalDateTime latestDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(APPLICATIONS_REQUEST)
            .set(
                APPLICATIONS_REQUEST.REQUEST_STATUS,
                ERequestStatus.getValue(ERequestStatus.REJECTED.name()))
            .set(APPLICATIONS_REQUEST.LATEST_DATE, latestDate)
            .set(APPLICATIONS_REQUEST.COMMENT, rejectApplicationRequestRequest.getComment())
            .where(
                APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(
                    rejectApplicationRequestRequest.getRequestId()))
            .execute();
  }

  @Override
  public void updateStatusApplication(Long requestId, String status, LocalDateTime latestDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(APPLICATIONS_REQUEST)
            .set(APPLICATIONS_REQUEST.REQUEST_STATUS, ERequestStatus.getValue(status))
            .set(APPLICATIONS_REQUEST.LATEST_DATE, latestDate)
            .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId))
            .execute();
  }

  @Override
  public int countListApplicationRequestReceive(QueryParam queryParam, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = getListConditionApplicationRequest(queryParam);
    final List<OrderField<?>> orderByList = getOrderFieldApplicationRequest(queryParam);
    TableLike<?> selectReview =
        dslContext
            .select(REVIEW_REQUEST.APPLICATIONS_REQUEST_ID, REVIEW_REQUEST.EMPLOYEE_ID)
            .from(REVIEW_REQUEST);

    final var query =
        dslContext
            .select(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID)
            .from(EMPLOYEE)
            .leftJoin(APPLICATIONS_REQUEST)
            .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(REQUEST_STATUS)
            .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
            .leftJoin(REQUEST_NAME)
            .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.REQUEST_NAME_ID))
            .leftJoin(REQUEST_TYPE)
            .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
            .where(conditions)
            .and(APPLICATIONS_REQUEST.APPROVER.eq(employeeId))
            .orderBy(orderByList)
            .unionAll(
                dslContext
                    .select(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID)
                    .from(EMPLOYEE)
                    .leftJoin(APPLICATIONS_REQUEST)
                    .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
                    .leftJoin(REQUEST_STATUS)
                    .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
                    .leftJoin(REQUEST_NAME)
                    .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.REQUEST_NAME_ID))
                    .leftJoin(REQUEST_TYPE)
                    .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
                    .leftJoin(selectReview)
                    .on(
                        selectReview
                            .field(REVIEW_REQUEST.APPLICATIONS_REQUEST_ID)
                            .eq(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID))
                    .where(conditions)
                    .and(selectReview.field(REVIEW_REQUEST.EMPLOYEE_ID).eq(employeeId))
                    .orderBy(orderByList));
    return dslContext.fetchCount(query);
  }

  @Override
  public int countListApplicationRequestSend(QueryParam queryParam, String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final List<Condition> conditions = getListConditionApplicationRequest(queryParam);
    final List<OrderField<?>> orderByList = getOrderFieldApplicationRequest(queryParam);

    final var query =
        dslContext
            .select(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID)
            .from(EMPLOYEE)
            .leftJoin(APPLICATIONS_REQUEST)
            .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(REQUEST_STATUS)
            .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
            .leftJoin(REQUEST_NAME)
            .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.REQUEST_NAME_ID))
            .leftJoin(REQUEST_TYPE)
            .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
            .where(conditions)
            .and(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(employeeId))
            .orderBy(orderByList);
    return dslContext.fetchCount(query);
  }

  @Override
  public void updateTaxEnrollmentByApplicationRequest(
      String employeeId, Long taxType, boolean status) {
    //    final DSLContext dslContext = DSL.using(connection.getConnection());
    //    final boolean isExist =
    //        dslContext.fetchExists(
    //            dslContext
    //                .select(EMPLOYEE_TAX.TAX_ID)
    //                .from(EMPLOYEE_TAX)
    //                .where(EMPLOYEE_TAX.EMPLOYEE_ID.eq(employeeId))
    //                .and(EMPLOYEE_TAX.TAX_ID.eq(taxType)));
    //    if (isExist) {
    //      final var query =
    //          dslContext
    //              .update(EMPLOYEE_TAX)
    //              .set(EMPLOYEE_TAX.TAX_STATUS, status)
    //              .where(EMPLOYEE_TAX.EMPLOYEE_ID.eq(employeeId))
    //              .and(EMPLOYEE_TAX.TAX_ID.eq(taxType));
    //    } else {
    //      final var query =
    //          dslContext
    //              .insertInto(
    //                  EMPLOYEE_TAX,
    //                  EMPLOYEE_TAX.EMPLOYEE_ID,
    //                  EMPLOYEE_TAX.TAX_ID,
    //                  EMPLOYEE_TAX.TAX_STATUS)
    //              .values(employeeId, taxType, status)
    //              .execute();
    //    }
  }

  @Override
  public void insertUpdateCompanyAssetsByApplicationRequest(String employeeId, LocalDate date) {}

  private List<Condition> getListConditionApplicationRequest(QueryParam queryParam) {
    final List<Condition> conditions = new ArrayList<>();
    final var mergeFilters =
        queryParam.filters.stream().collect(Collectors.groupingBy(filter -> filter.field));

    mergeFilters.forEach(
        (key, values) -> {
          Condition condition = DSL.noCondition();
          Condition requestTypeCondition = DSL.noCondition();
          for (QueryFilter filter : values) {

            final Field<?> field = field2Map.get(filter.field);

            if (Objects.isNull(field)) {
              throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILTER_INVALID);
            }
            if (filter.field.equals(REQUEST_TYPE_PARAM)) {
              requestTypeCondition = requestTypeCondition.or(queryHelper.condition(filter, field));
            } else {
              condition = condition.and(queryHelper.condition(filter, field));
            }
          }
          condition = condition.and(requestTypeCondition);
          conditions.add(condition);
        });
    return conditions;
  }

  private List<OrderField<?>> getOrderFieldApplicationRequest(QueryParam queryParam) {
    final List<OrderField<?>> orderByList = new ArrayList<>();

    if (null == queryParam || isEmpty(queryParam.orderByList)) {
      orderByList.add(APPLICATIONS_REQUEST.LATEST_DATE.desc());
    }

    for (OrderByClause clause : queryParam.orderByList) {

      final Field<?> field = field2Map.get(clause.field);

      if (Objects.isNull(field)) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ORDER_BY_INVALID);
      }
      if (clause.field.equals(IS_BOOKMARK_PARAM)) {
        if (clause.orderBy.equals(OrderBy.ASC)) {
          orderByList.add(when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).asc());
        } else {
          orderByList.add(when(APPLICATIONS_REQUEST.IS_BOOKMARK.isTrue(), 1).otherwise(2).desc());
        }
      } else {
        if (clause.orderBy.equals(OrderBy.ASC)) {
          orderByList.add(field.asc().nullsLast());
        } else {
          orderByList.add(field.desc().nullsLast());
        }
      }
    }
    return orderByList;
  }

  @Override
  public List<RequestStatusDto> getAllRequestStatus() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            REQUEST_STATUS.TYPE_ID.as("request_status_id"),
            REQUEST_STATUS.NAME.as("request_status_name"))
        .from(REQUEST_STATUS)
        .fetchInto(RequestStatusDto.class);
  }

  @Override
  public List<RequestTypeDto> getAllRequestType() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            REQUEST_TYPE.TYPE_ID.as("request_type_id"), REQUEST_TYPE.NAME.as("request_type_name"))
        .from(REQUEST_TYPE)
        .fetchInto(RequestTypeDto.class);
  }

  @Override
  public List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            REQUEST_NAME.REQUEST_NAME_ID.as("request_name_id"),
            REQUEST_NAME.NAME.as("request_name_name"))
        .from(REQUEST_NAME)
        .where(REQUEST_NAME.REQUEST_TYPE_ID.eq(requestTypeID))
        .fetchInto(RequestNameDto.class);
  }

  private String checkLevelOfManagerByRequestId(String employeeId, Long requestApplicationId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var level =
        dslContext
            .select(EMPLOYEE.LEVEL)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .fetchOneInto(Integer.class);

    final var maximumLevelAccept =
        dslContext
            .select(POLICY.MAXIMUM_LEVEL_ACCEPT)
            .from(APPLICATIONS_REQUEST)
            .leftJoin(REQUEST_NAME)
            .on(REQUEST_NAME.REQUEST_NAME_ID.eq(APPLICATIONS_REQUEST.REQUEST_NAME))
            .leftJoin(POLICY)
            .on(POLICY.POLICY_ID.eq(REQUEST_NAME.POLICY_ID))
            .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestApplicationId))
            .fetchOneInto(Integer.class);

    if (maximumLevelAccept == null || level == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NULL_LEVEL);
    }

    return level <= maximumLevelAccept && level > 0 ? "True" : "False";
  }

  private Select<?> getListForwarderByRequestId(Long requestId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(REVIEW_REQUEST.EMPLOYEE_ID)
        .from(REVIEW_REQUEST)
        .where(REVIEW_REQUEST.APPLICATIONS_REQUEST_ID.eq(requestId));
  }

  //  @Override
  //  public void changeIsRead(boolean isRead, Long requestId) {
  //    final DSLContext dslContext = DSL.using(connection.getConnection());
  //    final var queryIsRead =
  //        dslContext
  //            .select(APPLICATIONS_REQUEST.IS_READ)
  //            .from(APPLICATIONS_REQUEST)
  //            .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId))
  //            .fetchOneInto(Boolean.class);
  //    if (Boolean.TRUE.equals(queryIsRead)) {
  //      final var query =
  //          dslContext
  //              .update(APPLICATIONS_REQUEST)
  //              .set(APPLICATIONS_REQUEST.IS_READ, isRead)
  //              .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId))
  //              .execute();
  //    }
  //  }

  @Override
  public Optional<ApplicationRequestDto> getApplicationRequestDtoByRequestId(Long requestId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.as("requestId"),
            APPLICATIONS_REQUEST.EMPLOYEE_ID.as("employeeId"),
            REQUEST_NAME.NAME.as("requestName"),
            REQUEST_TYPE.NAME.as("requestType"),
            APPLICATIONS_REQUEST.COMMENT,
            APPLICATIONS_REQUEST.LATEST_DATE.as("latestDate"),
            APPLICATIONS_REQUEST.APPROVER.as("approveId"),
            APPLICATIONS_REQUEST.DATA)
        .from(APPLICATIONS_REQUEST)
        .leftJoin(REQUEST_NAME)
        .on(REQUEST_NAME.REQUEST_NAME_ID.eq(APPLICATIONS_REQUEST.REQUEST_NAME))
        .leftJoin(REQUEST_TYPE)
        .on(REQUEST_TYPE.TYPE_ID.eq(REQUEST_NAME.REQUEST_TYPE_ID))
        .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId))
        .fetchOptionalInto(ApplicationRequestDto.class);
  }

  @Override
  public List<ApplicationsRequestResponse> getListApplicationRequestReceiveByListId(
      QueryParam queryParam, String employeeId, List<Long> list) {
    final List<Condition> conditions = getListConditionApplicationRequest(queryParam);

    final List<OrderField<?>> orderByList = getOrderFieldApplicationRequest(queryParam);
    Condition condition = noCondition();
    for (Long id : list) {
      condition = condition.or(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(id));
    }
    conditions.add(condition);

    List<ApplicationsRequestResponse> applicationsRequestResponseForwardList =
        getListApplicationRequestReceiveForward(
                conditions, orderByList, queryParam.pagination, employeeId)
            .fetchInto(ApplicationsRequestResponse.class);

    List<ApplicationsRequestResponse> applicationsRequestResponseList =
        Stream.of(
                applicationsRequestResponseForwardList,
                getListApplicationRequestReceive(
                        conditions, orderByList, queryParam.pagination, employeeId)
                    .fetchInto(ApplicationsRequestResponse.class))
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());

    applicationsRequestResponseList.forEach(
        applicationsRequestResponse -> {
          applicationsRequestResponse.setChecked_by(
              getListForwarderByRequestId(applicationsRequestResponse.getApplication_request_id())
                  .fetchInto(String.class));
          applicationsRequestResponse.setIs_enough_level(
              checkLevelOfManagerByRequestId(
                  employeeId, applicationsRequestResponse.getApplication_request_id()));
          applicationsRequestResponse.setRequest_name(
              ERequestName.getLabel(applicationsRequestResponse.getRequest_name()));
          applicationsRequestResponse.setRequest_type(
              ERequestType.getLabel(applicationsRequestResponse.getRequest_type()));
          applicationsRequestResponse.setType(
              checkNotificationOrRequest(
                  employeeId, applicationsRequestResponse.getApplication_request_id()));
        });

    return applicationsRequestResponseList;
  }

  @Override
  public List<ApplicationsRequestResponse> getListApplicationRequestSendByListId(
      QueryParam queryParam, String employeeId, List<Long> list) {
    final List<Condition> conditions = getListConditionApplicationRequest(queryParam);

    final List<OrderField<?>> orderByList = getOrderFieldApplicationRequest(queryParam);
    Condition condition = noCondition();
    for (Long id : list) {
      condition = condition.or(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(id));
    }
    conditions.add(condition);

    List<ApplicationsRequestResponse> applicationsRequestResponseList =
        getListApplicationRequestSend(conditions, orderByList, queryParam.pagination, employeeId)
            .fetchInto(ApplicationsRequestResponse.class);

    applicationsRequestResponseList.forEach(
        applicationsRequestResponse -> {
          applicationsRequestResponse.setChecked_by(
              getListForwarderByRequestId(applicationsRequestResponse.getApplication_request_id())
                  .fetchInto(String.class));
          applicationsRequestResponse.setRequest_name(
              ERequestName.getLabel(applicationsRequestResponse.getRequest_name()));
          applicationsRequestResponse.setRequest_type(
              ERequestType.getLabel(applicationsRequestResponse.getRequest_type()));
          applicationsRequestResponse.setType(
              checkNotificationOrRequest(
                  employeeId, applicationsRequestResponse.getApplication_request_id()));
        });

    return applicationsRequestResponseList;
  }

  @Override
  public void createApplicationsRequest(ApplicationsRequestCreateRequest applicationsRequest) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .insertInto(
            APPLICATIONS_REQUEST,
            APPLICATIONS_REQUEST.EMPLOYEE_ID,
            APPLICATIONS_REQUEST.REQUEST_NAME,
            APPLICATIONS_REQUEST.REQUEST_STATUS,
            APPLICATIONS_REQUEST.DESCRIPTION,
            APPLICATIONS_REQUEST.APPROVER,
            APPLICATIONS_REQUEST.CREATE_DATE,
            APPLICATIONS_REQUEST.LATEST_DATE,
            APPLICATIONS_REQUEST.DURATION,
            APPLICATIONS_REQUEST.DATA,
            APPLICATIONS_REQUEST.IS_REMIND,
            APPLICATIONS_REQUEST.IS_BOOKMARK)
        .values(
            applicationsRequest.getCreateEmployeeId(),
            applicationsRequest.getRequestNameId(),
            applicationsRequest.getRequestStatusId(),
            applicationsRequest.getDescription(),
            applicationsRequest.getApprover(),
            applicationsRequest.getCreateDate(),
            applicationsRequest.getLatestDate(),
            applicationsRequest.getDuration(),
            applicationsRequest.getData(),
            applicationsRequest.getIsRemind(),
            applicationsRequest.getIsBookmark())
        .execute();
  }

  @Override
  public List<RequestTypeDto> getAllRequestTypeByEmployeeLevel(String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var level =
        dslContext
            .select(EMPLOYEE.LEVEL)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeId))
            .fetchOneInto(Integer.class);
    return dslContext
        .selectDistinct(
            REQUEST_TYPE.TYPE_ID.as("request_type_id"), REQUEST_TYPE.NAME.as("request_type_name"))
        .from(REQUEST_TYPE)
        .leftJoin(REQUEST_NAME)
        .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
        .leftJoin(POLICY)
        .on(POLICY.POLICY_ID.eq(REQUEST_NAME.POLICY_ID))
        .where(POLICY.MAXIMUM_LEVEL_ACCEPT.greaterOrEqual(level))
        .orderBy(REQUEST_TYPE.TYPE_ID)
        .fetchInto(RequestTypeDto.class);
  }

  @Override
  public String getDescriptionByRequestNameID(Long requestNameID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(REQUEST_NAME.DESCRIPTION)
        .from(REQUEST_NAME)
        .where(REQUEST_NAME.REQUEST_NAME_ID.eq(requestNameID))
        .fetchOneInto(String.class);
  }

  @Override
  public PolicyTypeAndNameResponse getPolicyByRequestNameID(Long requestNameID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(POLICY_TYPE.POLICY_TYPE_)
        //                , POLICY_TYPE.POLICY_NAME)
        .from(REQUEST_NAME)
        .leftJoin(POLICY)
        .on(REQUEST_NAME.POLICY_ID.eq(POLICY.POLICY_ID))
        .leftJoin(POLICY_TYPE)
        .on(POLICY_TYPE.POLICY_TYPE_ID.eq(POLICY.POLICY_TYPE_ID))
        .where(REQUEST_NAME.REQUEST_NAME_ID.eq(requestNameID))
        .fetchOneInto(PolicyTypeAndNameResponse.class);
  }

  @Override
  public Boolean checkPermissionToCreate(String employeeId, Long requestNameId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    final var level =
        dslContext
            .select(EMPLOYEE.LEVEL)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeId))
            .fetchOneInto(Integer.class);

    final var maximumLevelAccept =
        dslContext
            .select(POLICY.MAXIMUM_LEVEL_ACCEPT)
            .from(REQUEST_NAME)
            .leftJoin(POLICY)
            .on(POLICY.POLICY_ID.eq(REQUEST_NAME.POLICY_ID))
            .where(REQUEST_NAME.REQUEST_NAME_ID.eq(requestNameId))
            .fetchOneInto(Integer.class);

    if (maximumLevelAccept == null || level == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NULL_LEVEL);
    }
    return level <= maximumLevelAccept;
  }

  @Override
  public Boolean checkPermissionToApprove(String employeeId, Long requestNameId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return true;
    //    final var level =
    //            dslContext
    //                    .select(EMPLOYEE.LEVEL)
    //                    .from(EMPLOYEE)
    //                    .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeId))
    //                    .fetchOneInto(Integer.class);
    //
    //    final var minimumLevelAccept =
    //            dslContext
    //                    .select(POLICY.MINIMUM_LEVEL_ACCEPT)
    //                    .from(REQUEST_NAME)
    //                    .leftJoin(POLICY)
    //                    .on(POLICY.POLICY_ID.eq(REQUEST_NAME.POLICY_ID))
    //                    .where(REQUEST_NAME.REQUEST_NAME_ID.eq(requestNameId))
    //                    .fetchOneInto(Integer.class);
    //
    //    if (minimumLevelAccept == null || level == null) {
    //      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NULL_LEVEL);
    //    }
    //    return level <= minimumLevelAccept;
  }

  @Override
  public void createApproveTaxEnrollment(EmployeeTaxDto employeeTaxDto) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    //    boolean check =
    //        dslContext.fetchExists(
    //            dslContext
    //                .select()
    //                .from(EMPLOYEE_TAX)
    //                .where(EMPLOYEE_TAX.POLICY_TYPE_ID.eq(employeeTaxDto.getTaxTypeID())));
    //
    //    if (!check) {
    //      dslContext
    //          .insertInto(
    //              EMPLOYEE_TAX,
    //              EMPLOYEE_TAX.EMPLOYEE_ID,
    //              EMPLOYEE_TAX.POLICY_TYPE_ID,
    //              EMPLOYEE_TAX.TAX_STATUS)
    //          .values(
    //              employeeTaxDto.getEmployeeID(),
    //              employeeTaxDto.getTaxTypeID(),
    //              employeeTaxDto.getTaxStatus())
    //          .execute();
    //    }
  }

  private String checkNotificationOrRequest(String employeeId, Long requestId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final String managerId =
        dslContext
            .select(EMPLOYEE.MANAGER_ID)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .fetchOneInto(String.class);
    if (managerId == null) {
      return "Notification";
    }
    final String employeeIdSendRequest =
        dslContext
            .select(APPLICATIONS_REQUEST.EMPLOYEE_ID)
            .from(APPLICATIONS_REQUEST)
            .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId))
            .fetchOneInto(String.class);
    return managerId.equals(employeeIdSendRequest) ? "Notification" : "Request";
  }

  @Override
  public List<ApplicationRequestRemindResponse> getAllApplicationRequestToRemind(
      LocalDateTime checkDate) {
    List<ApplicationRequestRemindResponse> applicationRequestRemindResponses =
        getListRequestRemind(checkDate).fetchInto(ApplicationRequestRemindResponse.class);
    applicationRequestRemindResponses.forEach(
        applicationRequestRemindResponse -> {
          applicationRequestRemindResponse.setChecked_by(
              getListForwarderByRequestId(
                      applicationRequestRemindResponse.getApplication_request_id())
                  .fetchInto(String.class));
        });

    return applicationRequestRemindResponses;
  }

  @Override
  public void updateAllApplicationRequestRemind(Long requestId, boolean isRemind) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(APPLICATIONS_REQUEST)
            .set(APPLICATIONS_REQUEST.IS_REMIND, isRemind)
            .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId))
            .execute();
  }

  private Select<?> getListRequestRemind(LocalDateTime checkDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID,
            REQUEST_NAME.NAME.as("request_name"),
            REQUEST_TYPE.NAME.as("request_type"),
            APPLICATIONS_REQUEST.CREATE_DATE,
            EMPLOYEE.FULL_NAME,
            EMPLOYEE.EMPLOYEE_ID,
            APPLICATIONS_REQUEST.APPROVER)
        .from(EMPLOYEE)
        .leftJoin(APPLICATIONS_REQUEST)
        .on(APPLICATIONS_REQUEST.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(REQUEST_STATUS)
        .on(APPLICATIONS_REQUEST.REQUEST_STATUS.eq(REQUEST_STATUS.TYPE_ID))
        .leftJoin(REQUEST_NAME)
        .on(APPLICATIONS_REQUEST.REQUEST_NAME.eq(REQUEST_NAME.REQUEST_NAME_ID))
        .leftJoin(REQUEST_TYPE)
        .on(REQUEST_NAME.REQUEST_TYPE_ID.eq(REQUEST_TYPE.TYPE_ID))
        .where(APPLICATIONS_REQUEST.DURATION.le(checkDate))
        .and(APPLICATIONS_REQUEST.IS_REMIND.isFalse());
  }

  @Override
  public String getDataOfPolicy(Long requestNameId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(POLICY.DATA)
        .from(REQUEST_NAME)
        .leftJoin(POLICY)
        .on(REQUEST_NAME.POLICY_ID.eq(POLICY.POLICY_ID))
        .where(REQUEST_NAME.REQUEST_NAME_ID.eq(requestNameId))
        .fetchOneInto(String.class);
  }

  @Override
  public boolean checkExistRequestId(Long requestId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID)
            .from(APPLICATIONS_REQUEST)
            .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId)));
  }

  @Override
  public boolean checkAlreadyApproveOrReject(Long requestId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID)
            .from(APPLICATIONS_REQUEST)
            .where(APPLICATIONS_REQUEST.APPLICATION_REQUEST_ID.eq(requestId))
            .and(
                APPLICATIONS_REQUEST
                    .REQUEST_STATUS
                    .eq(ERequestStatus.getValue(ERequestStatus.REJECTED.name()))
                    .or(
                        APPLICATIONS_REQUEST.REQUEST_STATUS.eq(
                            ERequestStatus.getValue(ERequestStatus.APPROVED.name())))));
  }
}