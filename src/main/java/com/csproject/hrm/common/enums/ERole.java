package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.ROLE_INVALID;

@NoArgsConstructor
public enum ERole {
    ROLE_ADMIN("ADMIN", 1),
    ROLE_MANAGER("MANAGER", 2),
    ROLE_USER("USER", 3);

    private String label;
    private long value;

    ERole(String label, long value) {
        this.label = label;
        this.value = value;
    }

    public static long of(String role) {
        for (ERole eRole : ERole.values()) {
            if (eRole.label.equalsIgnoreCase(role)) {
                return eRole.value;
            }
        }
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ROLE_INVALID);
    }

}
