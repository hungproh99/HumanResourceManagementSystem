package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeDetailRequest {
  @NotBlank(message = "full_name must not be blank!")
  private String full_name;

  @NotBlank(message = "employee_id must not be blank!")
  private String employee_id;

  @Past(message = "startDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate start_date;

  @Past(message = "endDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate end_date;

  private Boolean working_status;

  @NotBlank(message = "contract_url must not be blank!")
  private String contract_url;

  @NotBlank(message = "phone_number must not be blank!")
  private String phone_number;

  private Long grade_id;

  @Past(message = "birth_date must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birth_date;

  @NotBlank(message = "company_emails must not be blank!")
  private String company_email;

  @NotBlank(message = "gender must not be blank!")
  private String gender;

  private String marital_status;
  private Long office_id;
  private Long job_id;
  private Long area_id;
  private Long working_contract_id;
  private Long working_place_id;
  private String avatar;
}