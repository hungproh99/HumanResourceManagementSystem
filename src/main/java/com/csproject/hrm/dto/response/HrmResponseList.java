package com.csproject.hrm.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrmResponseList {
  private List<HrmResponse> hrmResponse;
  private int total;
}