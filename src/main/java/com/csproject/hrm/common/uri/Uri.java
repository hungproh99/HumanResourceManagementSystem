package com.csproject.hrm.common.uri;

public class Uri {
  public static final String REQUEST_MAPPING = "/hrm/api";
  public static final String URI_LOGIN = "/login";
  public static final String URI_CHANGE_PASSWORD = "/change_password";
  public static final String URI_FORGOT_PASSWORD = "/forgot_password";
  public static final String URI_GET_ALL_EMPLOYEE = "/get_all_employee";
  public static final String URI_INSERT_EMPLOYEE = "/add_employee";
  public static final String URI_INSERT_MULTI_EMPLOYEE = "/import_csv_employee";
  public static final String URI_LIST_WORKING_TYPE = "/list_working_type";
  public static final String URI_LIST_EMPLOYEE_TYPE = "/list_employee_type";
  public static final String URI_LIST_OFFICE = "/list_office";
  public static final String URI_LIST_AREA = "/list_area";
  public static final String URI_LIST_JOB = "/list_job";
  public static final String URI_LIST_GRADE = "/list_grade/{job_id}";
  public static final String URI_LIST_ROLE_TYPE = "/list_role_type";
  public static final String URI_UPDATE_EMPLOYEE = "/update_employee/{employee_id}";
  public static final String URI_DOWNLOAD_CSV_EMPLOYEE = "/download_csv_employee";
  public static final String URI_GET_LIST_MANAGER = "/list_manager";
  public static final String REQUEST_DETAIL_MAPPING = "/employee/detail";
  public static final String URI_GET_MAIN_DETAIL = "/main";
  public static final String URI_GET_TAX_AND_INSURANCE = "/tax_and_insurance";
  public static final String URI_GET_ADDITIONAL_INFO = "/add_info";
  public static final String URI_GET_BANK_INFO = "/bank";
  public static final String URI_GET_EDU_INFO = "/edu";
  public static final String URI_GET_WORKING_HISTORY_INFO = "/working_history";
  public static final String URI_GET_RELATIVE_INFO = "/relative";
  public static final String URI_UPDATE_MAIN_DETAIL = "/main/update";
  public static final String URI_UPDATE_TAX_AND_INSURANCE = "/tax_and_insurance/update";
  public static final String URI_UPDATE_BANK_INFO = "/bank/update";
  public static final String URI_UPDATE_EDUCATION_INFO = "/education/update";
  public static final String URI_UPDATE_WORKING_HISTORY_INFO = "/working_history/update";
  public static final String URI_UPDATE_RELATIVE_INFO = "/relative/update";
}