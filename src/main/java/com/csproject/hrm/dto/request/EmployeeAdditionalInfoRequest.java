package com.csproject.hrm.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static com.csproject.hrm.common.constant.Constants.EMAIL_VALIDATION;
import static com.csproject.hrm.common.constant.Constants.NUMERIC_VALIDATION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAdditionalInfoRequest {
  @NotBlank(message = "address must not be blank!")
  private String address;

  @NotBlank(message = "place_of_residence must not be blank!")
  private String place_of_residence;

  @NotBlank(message = "place_of_origin must not be blank!")
  private String place_of_origin;

  @NotBlank(message = "nationality must not be blank!")
  private String nationality;

  @NotBlank(message = "card_id must not be blank!")
  @Pattern(regexp = NUMERIC_VALIDATION, message = "card_id accept numeric only!")
  private String card_id;

  @NotBlank(message = "provideDate must not be blank!")
  @Past(message = "provideDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate provideDate;

  @NotBlank(message = "providePlace must not be blank!")
  private String providePlace;

  @Pattern(regexp = EMAIL_VALIDATION, message = "personal_email is not valid!!")
  private String personal_email;

  @NotBlank(message = "phone_number must not be blank!")
  @Pattern(regexp = NUMERIC_VALIDATION, message = "card_id accept numeric only!")
  private String phone_number;

  private String nick_name;

  private String facebook;

  @NotBlank(message = "employee_id must not be blank!")
  private String employee_id;
}