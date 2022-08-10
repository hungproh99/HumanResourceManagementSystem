package com.csproject.hrm.common.sample;

import com.csproject.hrm.common.utils.DateUtils;
import com.csproject.hrm.dto.response.*;

import java.util.*;

public class PolicyDataSample {
  public static final ListPolicyResponse LIST_POLICY_RESPONSE =
      new ListPolicyResponse(
          new ArrayList<>(
              Arrays.asList(
                  new PolicyResponse(
                      6L,
                      "ALL01",
                      "Allowance",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Compensation Benefit Policy"),
                  new PolicyResponse(
                      11L,
                      "INSU01",
                      "Insurance",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Insurance Policy"),
                  new PolicyResponse(
                      7L,
                      "NMR01",
                      "Nomination right",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Compensation Benefit Policy"),
                  new PolicyResponse(
                      12L,
                      "OT01",
                      "OT",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Overtime"),
                  new PolicyResponse(
                      8L,
                      "PL01",
                      "Paid Leave",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Leave Policy"),
                  new PolicyResponse(
                      10L,
                      "TX01",
                      "TAx",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Tax Policy"),
                  new PolicyResponse(
                      9L,
                      "UPL01",
                      "Unpaid Leave",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Leave Policy"),
                  new PolicyResponse(
                      1L,
                      "WT01",
                      "Working Time",
                      null,
                      DateUtils.convert("2022-01-01"),
                      DateUtils.convert("2022-01-01"),
                      null,
                      true,
                      "Working Rule"))),
          8);

  public static final List<PolicyCategoryResponse> POLICY_CATEGORY_RESPONSES =
      new ArrayList<>(
          Arrays.asList(
              new PolicyCategoryResponse(1L, "Working Rule"),
              new PolicyCategoryResponse(2L, "Compensation Benefit Policy"),
              new PolicyCategoryResponse(3L, "Tax Policy"),
              new PolicyCategoryResponse(4L, "Insurance Policy"),
              new PolicyCategoryResponse(5L, "Leave Policy"),
              new PolicyCategoryResponse(6L, "Salary Policy"),
              new PolicyCategoryResponse(7L, "Overtime")));
}