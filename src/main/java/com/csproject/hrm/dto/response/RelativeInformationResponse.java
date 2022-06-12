package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelativeInformationResponse {
  private Long relative_id;
  private String parent_name;
  private LocalDate birth_date;
  private Long type_id;
  private String status;
  private String contact;
}