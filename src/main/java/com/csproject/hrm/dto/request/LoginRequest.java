package com.csproject.hrm.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
  private String email;
  private String password;
}