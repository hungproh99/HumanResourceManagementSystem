package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.OFFICE_INVALID;

public enum EOffice {
    HN_OFFICE("Ha Noi Office", 1);

    private String label;
    private long value;

    EOffice(String label, long value) {
        this.label = label;
        this.value = value;
    }

    public static long of(String role) {
        for (EOffice eOffice : EOffice.values()) {
            if (eOffice.label.equalsIgnoreCase(role)) {
                return eOffice.value;
            }
        }
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, OFFICE_INVALID);
    }
}
