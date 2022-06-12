package com.csproject.hrm.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
  private Long bank_id;
  private String name_bank;
  private String address;
  private String account_number;
  private String account_name;
}