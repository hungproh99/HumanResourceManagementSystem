package com.csproject.hrm.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
  private String email;
  private String old_password;
  private String new_password;
  private String re_password;
}