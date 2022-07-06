package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.ASSET_INVALID;

public enum EAsset {
  COMPUTER("Computer", 1);

  private String label;
  private long value;

  EAsset(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String asset) {
    for (EAsset eAsset : EAsset.values()) {
      if (eAsset.name().equalsIgnoreCase(asset)) {
        return eAsset.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, ASSET_INVALID);
  }
}
