package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeDetailRequest {
  @NotBlank(message = "place_of_residence must not be blank!")
  private String full_name;

  @NotBlank(message = "place_of_residence must not be blank!")
  private String employee_id;

  @NotBlank(message = "place_of_residence must not be blank!")
  private LocalDate start_date;

  @NotBlank(message = "place_of_residence must not be blank!")
  private LocalDate end_date;

  @NotBlank(message = "place_of_residence must not be blank!")
  private Boolean working_status;

  @NotBlank(message = "place_of_residence must not be blank!")
  private String contract_url;

  @NotBlank(message = "place_of_residence must not be blank!")
  private String phone_number;

  private Long grade_id;

  @NotBlank(message = "place_of_residence must not be blank!")
  private LocalDate birth_date;

  @NotBlank(message = "place_of_residence must not be blank!")
  private String company_email;

  @NotBlank(message = "place_of_residence must not be blank!")
  private String gender;

  private String marital_status;
  private Long office_id;
  private Long job_id;
  private Long area_id;
  private Long working_contract_id;
  private Long working_place_id;
  private String avatar;
}