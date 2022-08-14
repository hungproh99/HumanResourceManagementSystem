package com.csproject.hrm.common.enums;

import com.csproject.hrm.exception.CustomErrorException;
import org.springframework.http.HttpStatus;

import static com.csproject.hrm.common.constant.Constants.BONUS_INVALID;

public enum EBonus {
  PROJECT_BONUS("Project bonus", 1),
  REWARD_BONUS("Reward bonus", 2),
  KPI_BONUS("KPI bonus", 3),
  OUT_SOURCE_BONUS("Out source bonus", 4);

  private String label;
  private long value;

  EBonus(String label, long value) {
    this.label = label;
    this.value = value;
  }

  public static String getLabel(String bonus) {
    for (EBonus eBonus : EBonus.values()) {
      if (eBonus.name().equalsIgnoreCase(bonus)) {
        return eBonus.label;
      }
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, BONUS_INVALID);
  }
}
