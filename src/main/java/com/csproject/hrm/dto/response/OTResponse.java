package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OTResponse {
  private String overtime_type;
  private List<OTDetailResponse> otDetailResponseList;
  private Double totalOTPointByType;
}
