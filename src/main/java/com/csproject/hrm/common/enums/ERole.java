package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.ROLE_INVALID;

@NoArgsConstructor
public enum ERole {
  ROLE_ADMIN("Admin", 1),
  ROLE_MANAGER("Manager", 2),
  ROLE_USER("User", 3);

  private String label;
  private long value;

  ERole(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String role) {
    for (ERole eRole : ERole.values()) {
      if (eRole.name().equalsIgnoreCase(role)) {
        return eRole.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, ROLE_INVALID);
  }

  public static Long getValue(String role) {
    for (ERole eRole : ERole.values()) {
      if (eRole.name().equalsIgnoreCase(role)) {
        return eRole.value;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, ROLE_INVALID);
  }
}
