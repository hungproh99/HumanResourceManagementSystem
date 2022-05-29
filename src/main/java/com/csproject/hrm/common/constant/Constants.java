package com.csproject.hrm.common.constant;

public class Constants {
    public static final String EMAIL_VALIDATION = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";

    public static final String MY_EMAIL = "huynq08120@gmail.com";
    public static final String FRIEND_EMAIL = "huynb0812@gmail.com";
    public static final String NOT_EMPTY_EMAIL = "Email can't not empty";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String NOT_EMPTY_PASSWORD = "Password can't not empty";
    public static final String NOT_EXIST_USER_WITH = "Don't have any user with ";
    public static final String WRONG_OLD_PASSWORD = "Old-password is wrong";
    public static final String NOT_MATCH_NEW_PASSWORD = "Re-password must match with new-password";
    public static final String NOT_SAME_OLD_PASSWORD = "New-password don't same old-password";
    public static final String SPECIAL_CHARACTER = "!@#$%^&*()_+";
    public static final String SEND_PASSWORD_SUBJECT = "SEND NEW PASSWORD";
    public static final String SEND_PASSWORD_TEXT = "Hello %s this is new reset password after you forgot %s \n Please don't send it for anyone";
    public static final String REQUEST_FAIL = "This request is failed";
    public static final String REQUEST_SUCCESS = "This request is successful";

}
