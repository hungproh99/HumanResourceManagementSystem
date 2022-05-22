package com.csproject.hrm.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NonNull
    @Email
    @NotEmpty
    private String email;
    @NonNull
    @NotEmpty
    private String password;
}
