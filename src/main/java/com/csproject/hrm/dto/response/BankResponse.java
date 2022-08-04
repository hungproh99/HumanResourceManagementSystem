package com.csproject.hrm.dto.response;

import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
  @Positive(message = "")
  private Long bank_id;

  @NotEmpty(message = "")
  @NotBlank(message = "")
  private String name_bank;

  @NotNull(message = "")
  private String address;

  @NotNull(message = "")
  private String account_number;

  @NotNull(message = "")
  private String account_name;
}