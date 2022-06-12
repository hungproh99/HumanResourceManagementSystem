package com.csproject.hrm.common.constant;

public class Constants {
  public static final String EMAIL_VALIDATION =
      "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
  public static final String FROM_EMAIL = "huynq08120@gmail.com";
  public static final String TO_EMAIL = "huynb0812@gmail.com";
  public static final String NOT_EMPTY_EMAIL = "Email can't not empty";
  public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
  public static final String NOT_EMPTY_PASSWORD = "Password can't not empty";
  public static final String NOT_EXIST_USER_WITH = "Don't have any user with ";
  public static final String WRONG_OLD_PASSWORD = "Old-password is wrong";
  public static final String NOT_MATCH_NEW_PASSWORD = "Re-password must match with new-password";
  public static final String NOT_SAME_OLD_PASSWORD = "New-password don't same old-password";
  public static final String SPECIAL_CHARACTER = "!@#$%^&*()_+";
  public static final String SEND_PASSWORD_SUBJECT = "SEND NEW PASSWORD";
  public static final String SEND_PASSWORD_TEXT =
      "<p style=\"color:black;\">Hello <strong>%s</strong> this is new reset password after you forgot <strong>%s</strong>"
          + "<br>Please don't send it for anyone"
          + "<br>You can change password in this link <a href=\"http://localhost:3000/forgot\">Change Password</a></p>";
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
  public static final String DAY = " day ";
  public static final String PAGING = "paging";
  public static final String FILTER = "filter";
  public static final String ORDER_BY = "orderBy";
  public static final String COMMA = ",";
  public static final String COLON = ":";
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
  public static final String DOMAIN_EMAIL = "@fpt.edu.vn";
  public static final String FILL_NOT_FULL = "Please fill full in form";
  public static final String CSV_NULL_DATA = "Field Csv null data";
  public static final String PHONE_VALIDATION = "^\\d{10,12}$";
  public static final String INVALID_PHONE_FORMAT = "Invalid phone format";
  public static final String INVALID_OFFSET = "Invalid offset";
  public static final String INVALID_LIMIT = "Invalid limit";
  public static final String WORK_STATUS_INVALID = "Invalid Work Status";
  public static final String NO_DATA = "Don't have any data";
  public static final String NO_EMPLOYEE_WITH_ID = "Don't have employee with ";
  public static final String CAN_NOT_WRITE_CSV = "Can't write csv ";
  public static final String ONLY_UPLOAD_CSV = "Only upload csv file ";
  public static final String WRONG_NUMBER_FORMAT = "Wrong number format ";
}
