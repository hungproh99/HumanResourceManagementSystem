package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
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

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate start_date;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate end_date;

  private Boolean working_status;

  @NotBlank(message = "contract_url must not be blank!")
  private String contract_url;

  @NotBlank(message = "phone_number must not be blank!")
  private String phone_number;

  @Positive(message = "grade_id must be a positive number!")
  private Long grade_id;

  @Past(message = "birth_date must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birth_date;

  @NotBlank(message = "company_emails must not be blank!")
  private String company_email;

  @NotBlank(message = "gender must not be blank!")
  private String gender;

  private String marital_status;

  @Positive(message = "office_id must be a positive number!")
  private Long office_id;

  @Positive(message = "job_id must be a positive number!")
  private Long job_id;

  @Positive(message = "area_id must be a positive number!")
  private Long area_id;

  @Positive(message = "working_contract_id must be a positive number!")
  private Long working_contract_id;

  @Positive(message = "working_place_id must be a positive number!")
  private Long working_place_id;
}