package com.csproject.hrm.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "applications_request")
public class ApplicationsRequest {
  @Id
  @Column(name = "application_request_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Column(name = "description")
  private String description;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "request_type")
  private RequestType requestType;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "request_name")
  private RequestName requestName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "request_status")
  private RequestStatus requestStatus;

  @Column(name = "create_date")
  private LocalDateTime createDate;

  @Column(name = "duration")
  private LocalDateTime duration;

  @Column(name = "latest_date")
  private LocalDateTime latestDate;

  @Column(name = "approver")
  private String approver;

  @Column(name = "forward")
  private String forward;

  @Column(name = "is_bookmark")
  @Type(type = "boolean")
  private Boolean isBookmark;

  @Column(name = "is_remind")
  @Type(type = "boolean")
  private Boolean isRemind;
}