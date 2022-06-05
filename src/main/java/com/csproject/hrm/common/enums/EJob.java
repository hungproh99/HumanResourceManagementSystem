package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.JOB_INVALID;

public enum EJob {
    IT("IT", 1);

    private String label;
    private long value;

    EJob(String label, long value) {
        this.label = label;
        this.value = value;
    }

    public static long of(String role) {
        for (EJob eJob : EJob.values()) {
            if (eJob.label.equalsIgnoreCase(role)) {
                return eJob.value;
            }
        }
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, JOB_INVALID);
    }
}
