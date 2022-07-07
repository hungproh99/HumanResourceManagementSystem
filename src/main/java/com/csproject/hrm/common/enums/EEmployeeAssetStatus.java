package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.EMPLOYEE_ASSET_STATUS_INVALID;

public enum EEmployeeAssetStatus {
  OVER_DUE("Over due", 1);

  private String label;
  private long value;

  EEmployeeAssetStatus(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String asset) {
    for (EEmployeeAssetStatus eEmployeeAssetStatus : EEmployeeAssetStatus.values()) {
      if (eEmployeeAssetStatus.name().equalsIgnoreCase(asset)) {
        return eEmployeeAssetStatus.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, EMPLOYEE_ASSET_STATUS_INVALID);
  }
}
