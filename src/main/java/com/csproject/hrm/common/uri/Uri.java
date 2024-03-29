package com.csproject.hrm.common.uri;

public class Uri {
  public static final String REQUEST_MAPPING = "/hrm/api";
  public static final String URI_LOGIN = "/login";
  public static final String URI_CHANGE_PASSWORD = "/change_password";
  public static final String URI_FORGOT_PASSWORD = "/forgot_password";
  public static final String URI_GET_ALL_EMPLOYEE = "/get_all_employee";
  public static final String URI_GET_LIST_MANAGER_HIGHER_OF_AREA =
      "/get_list_manager_higher_of_area";

  public static final String URI_GET_LIST_MANAGER_LOWER_OF_AREA = "/get_list_manager_lower_of_area";
  public static final String URI_INSERT_EMPLOYEE = "/add_employee";
  public static final String URI_INSERT_MULTI_EMPLOYEE_BY_CSV = "/import_csv_employee";
  public static final String URI_LIST_WORKING_TYPE = "/list_working_type";
  public static final String URI_LIST_EMPLOYEE_TYPE = "/list_employee_type";
  public static final String URI_LIST_OFFICE = "/list_office";
  public static final String URI_LIST_AREA = "/list_area";
  public static final String URI_LIST_JOB = "/list_job";
  public static final String URI_LIST_GRADE = "/list_grade/{job_id}";
  public static final String URI_LIST_ROLE_TYPE = "/list_role_type";
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
  public static final String URI_GET_LIST_TIMEKEEPING = "/list_all_timekeeping";
  public static final String URI_DOWNLOAD_CSV_TIMEKEEPING = "/download_csv_timekeeping";
  public static final String URI_GET_DETAIL_TIMEKEEPING = "/list_detail_timekeeping";
  public static final String URI_GET_CHECKIN_CHECKOUT = "/checkin_checkout";
  public static final String URI_DOWNLOAD_EXCEL_EMPLOYEE = "/download_excel_employee";
  public static final String URI_DOWNLOAD_EXCEL_TIMEKEEPING = "/download_excel_timekeeping";
  public static final String URI_INSERT_MULTI_EMPLOYEE_BY_EXCEL = "/import_excel_employee";
  public static final String URI_GET_LIST_APPLICATION_REQUEST_SEND =
      "/list_all_application_request_send";
  public static final String URI_GET_LIST_APPLICATION_REQUEST_RECEIVE =
      "/list_all_application_request_receive";
  public static final String URI_UPDATE_CHECK_APPLICATION_QUEST =
      "/update_checked_application_request";

  //  public static final String URI_UPDATE_IS_READ = "/update_is_read";
  public static final String URI_UPDATE_APPROVE_APPLICATION_REQUEST =
      "/update_approve_application_request";
  public static final String URI_UPDATE_REJECT_APPLICATION_REQUEST =
      "/update_reject_application_request";

  public static final String URI_GET_LIST_EMPLOYEE_NAME_AND_ID = "/list_employee_name_id";

  public static final String URI_DOWNLOAD_EXCEL_REQUEST_RECEIVE = "/download_excel_request_receive";
  public static final String URI_DOWNLOAD_CSV_REQUEST_RECEIVE = "/download_csv_request_receive";
  public static final String URI_DOWNLOAD_EXCEL_REQUEST_SEND = "/download_excel_request_send";
  public static final String URI_DOWNLOAD_CSV_REQUEST_SEND = "/download_csv_request_send";
  public static final String URI_GET_ALL_PERSONAL_SALARY_MONTHLY =
      "/get_all_personal_salary_monthly";
  public static final String URI_GET_ALL_MANAGEMENT_SALARY_MONTHLY =
      "/get_all_management_salary_monthly";
  public static final String URI_GET_SALARY_MONTHLY_DETAIL = "/get_salary_monthly_detail";
  public static final String URI_DOWNLOAD_EXCEL_PERSONAL_SALARY_MONTHLY =
      "/download_excel_personal_salary_monthly";
  public static final String URI_DOWNLOAD_CSV_PERSONAL_SALARY_MONTHLY =
      "/download_csv_personal_salary_monthly";

  public static final String URI_DOWNLOAD_EXCEL_MANAGEMENT_SALARY_MONTHLY =
      "/download_excel_management_salary_monthly";
  public static final String URI_DOWNLOAD_CSV_MANAGEMENT_SALARY_MONTHLY =
      "/download_csv_management_salary_monthly";
  public static final String URI_UPDATE_DEDUCTION_SALARY = "/update_deduction_salary";
  public static final String URI_DELETE_DEDUCTION_SALARY = "/delete_deduction_salary";
  public static final String URI_UPDATE_BONUS_SALARY = "/update_bonus_salary";
  public static final String URI_DELETE_BONUS_SALARY = "/delete_bonus_salary";
  public static final String URI_UPDATE_ADVANCE_SALARY = "/update_advance_salary";
  public static final String URI_DELETE_ADVANCE_SALARY = "/delete_advance_salary";
  public static final String URI_UPDATE_APPROVE_SALARY_MONTHLY = "/update_approve_salary_monthly";
  public static final String URI_UPDATE_REJECT_SALARY_MONTHLY = "/update_reject_salary_monthly";
  public static final String URI_UPDATE_CHECKED_SALARY_MONTHLY = "/update_checked_salary_monthly";
  public static final String URI_GET_LIST_DEDUCTION_TYPE = "/get_list_deduction_type";
  public static final String URI_GET_LIST_BONUS_TYPE = "/get_list_bonus_type";
  public static final String URI_GENERATE_SALARY_MONTHLY = "/generate_salary_monthly";
}
