package com.csproject.hrm.common.constant;

public class Constants {
  public static final String EMAIL_VALIDATION =
      "^[A-Za-z][A-Za-z0-9_\\.]{3,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
  public static final String PASSWORD_VALIDATION =
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+]).{8,32}$";
  public static final String ALPHANUMERIC_VALIDATION = "^[\\p{L} .'-]+$";
  public static final String NUMERIC_VALIDATION = "^(?:\\d*|)$";
  public static final String FROM_EMAIL = "huynq08120@gmail.com";
  public static final String TO_EMAIL = "hihihd37@gmail.com";
  public static final String NOT_EMPTY_EMAIL = "Email can't not empty";
  public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
  public static final String NOT_EMPTY_PASSWORD = "Password can't not empty";
  public static final String INVALID_ALPHANUMERIC_VALIDATION = "Invalid alphanumeric format";
  public static final String NOT_EXIST_USER_WITH = "Don't have any user with ";
  public static final String WRONG_OLD_PASSWORD = "Old-password is wrong";
  public static final String NOT_MATCH_NEW_PASSWORD = "Re-password must match with new-password";
  public static final String NOT_SAME_OLD_PASSWORD = "New-password don't same old-password";
  public static final String SPECIAL_CHARACTER = "!@#$%^&*()_+";
  public static final String SEND_PASSWORD_SUBJECT = "SEND NEW PASSWORD";

  public static final String REQUEST_FAIL = "This request is failed";
  public static final String REQUEST_SUCCESS = "This request is successful";
  public static final String ORDER_BY_INVALID = "Invalid OrderBy";
  public static final String FILTER_INVALID = "Invalid Filter";
  public static final String PAGING_INVALID = "Invalid Paging";
  public static final String PERCENT_CHARACTER = "%";
  public static final String DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";
  public static final String DASH_CHARACTER = "-";
  public static final int ZERO_NUMBER = 0;
  public static final String ZERO_CHARACTER = "0";
  public static final int ONE_NUMBER = 1;
  public static final String ONE_CHARACTER = "1";
  public static final int TWO_NUMBER = 2;

  public static final String EMPLOYEE_ID = "employeeId";
  public static final String FULL_NAME = "fullName";
  public static final String EMAIL = "email";
  public static final String PHONE = "phone";
  public static final String GENDER = "gender";
  public static final String GRADE = "grade";
  public static final String OFFICE_NAME = "office_name";
  public static final String AREA_NAME = "area_name";
  public static final String POSITION_NAME = "position_name";
  public static final String WORKING_NAME = "working_name";
  public static final String SENIORITY = "seniority";
  public static final String YEAR = " year ";
  public static final String MONTH = " month ";
  public static final String DAY = " day";
  public static final String PAGING = "paging";
  public static final String FILTER = "filter";
  public static final String ORDER_BY = "orderBy";
  public static final String COMMA = ",";
  public static final String COLON = ":";
  public static final String SEPARATOR = "\\|";
  public static final String OFFSET = "offset";
  public static final String LIMIT = "limit";
  public static final String INVALID_NUMBER_FORMAT = "Invalid Number Format";
  public static final String ERROR_AUTHORIZED = "Error: Unauthorized";
  public static final String AUTHORIZATION = "Authorization";
  public static final String BEARER = "Bearer ";
  public static final String PATTERN = "/hrm/api/**";
  public static final String WORKING_STATUS = "working_status";
  public static final String ROLE_INVALID = "Invalid Role";
  public static final String WORKING_TYPE_INVALID = "Invalid Working Type";
  public static final String EMPLOYEE_TYPE_INVALID = "Invalid Employee Type";
  public static final String AREA_INVALID = "Invalid Area";
  public static final String JOB_INVALID = "Invalid Job";
  public static final String OFFICE_INVALID = "Invalid Office";
  public static final String DOMAIN_EMAIL = "@thegorf.tk";
  public static final String FILL_NOT_FULL = "Please fill full in form";
  public static final String CSV_NULL_DATA = "Field Csv null data";
  public static final String PHONE_VALIDATION = "^\\d{9,10}$";
  public static final String INVALID_PHONE_FORMAT = "Invalid phone format";
  public static final String INVALID_OFFSET = "Invalid offset";
  public static final String INVALID_LIMIT = "Invalid limit";
  public static final String WORK_STATUS_INVALID = "Invalid Work Status";
  public static final String NO_DATA = "Don't have any data";
  public static final String NO_EMPLOYEE_WITH_ID = "Don't have employee with ";
  public static final String CAN_NOT_WRITE_CSV = "Can't write csv ";
  public static final String ONLY_UPLOAD_CSV = "Only upload csv file ";
  public static final String WRONG_NUMBER_FORMAT = "Wrong number format ";
  public static final String POSITION = "position";
  public static final String OFFICE = "office";
  public static final String AREA = "area";

  public static final String DATE = "date";
  public static final String CURRENT_DATE = "current_date";
  public static final String TIMEKEEPING_STATUS = "timekeeping_status";
  public static final String FIRST_CHECK_IN = "first_check_in";
  public static final String LAST_CHECK_OUT = "last_check_out";
  public static final String TIMEKEEPING_STATUS_INVALID = "Invalid Timekeeping Status";
  public static final String CHECK_IN_CHECK_OUTS = "check_in_check_outs";

  public static final String REQUEST_TYPE_INVALID = "Invalid Request Type";
  public static final String REQUEST_STATUS_INVALID = "Invalid Request Status";
  public static final String REQUEST_NAME_INVALID = "Invalid Request Name";
  public static final String GRADE_TYPE_INVALID = "Invalid Grade Type";
  public static final String CREATE_DATE = "createDate";
  public static final String IS_BOOKMARK = "is_bookmark";
  public static final String REQUEST_STATUS_NAME = "request_status_name";
  public static final String REQUEST_TYPE = "request_type";
  public static final String REQUEST_TITLE = "request_title";
  public static final String ACTIVE = "Active";
  public static final String DEACTIVE = "Deactive";
  public static final String ONLY_UPLOAD_EXCEL = "Only upload excel file ";
  public static final String TOTAL_WORKING_TIME = "total_working_time";
  public static final String REQUEST_TYPE_PARAM = "requestType";
  public static final String IS_BOOKMARK_PARAM = "isBookmark";
  public static final String CHANGE_STATUS_TIME = "change_status_time";
  public static final String UNAUTHORIZED_ERROR = "Unauthorized error";
  public static final String REQUEST_STATUS_PARAM = "requestStatus";
  public static final String TAX_INVALID = "Invalid Tax";
  public static final String INSURANCE_INVALID = "Invalid Insurance";
  public static final String CATEGORY_ID = "categoryID";
  public static final String POLICY_ID = "policyID";
  public static final String POLICY_TYPE_INVALID = "Invalid Policy Type";
  public static final String POLICY_NAME_INVALID = "Invalid Policy Name";
  public static final String POLICY_CATEGORY_INVALID = "Invalid Category Type";
  public static final String OVER_TIME_INVALID = "Invalid Overtime";
  public static final String ASSET_INVALID = "Invalid Asset";
  public static final String EMPLOYEE_ASSET_STATUS_INVALID = "Invalid Employee Asset Status";
  public static final String DEDUCTION_INVALID = "Invalid Deduction Type";
  public static final String BONUS_INVALID = "Invalid Bonus Type";
  public static final String NULL_LEVEL = "Level is null";

  public static final String REQUEST_ID_PARAM = "requestId";
  public static final String START_DATE = "startDate";
  public static final String END_DATE = "endDate";
  public static final String SALARY_STATUS_PARAM = "salaryStatus";
  public static final String NULL_PARAMETER = "Null Parameter";
  public static final String LATEST_DATE = "latestDate";
}