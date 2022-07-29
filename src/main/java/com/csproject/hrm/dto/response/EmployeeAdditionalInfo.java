package com.csproject.hrm.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAdditionalInfo {
  private String address;
  private String place_of_residence;
  private String place_of_origin;
  private String nationality;
  private String card_id;
  private String provideDate;
  private String providePlace;
  private String personal_email;
  private String phone_number;
  private String nick_name;
  private String facebook;
}