package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String id;
    private String email;
    private List<String> roles;
    private final String tokenType = "Bearer";
    private String accessToken;
}
