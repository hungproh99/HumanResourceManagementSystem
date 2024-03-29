package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Column(name = "description", length = 1000)
  private String description;

  @Column(name = "comment", length = 1000)
  private String comment;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "request_name")
  private RequestName requestName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "request_status")
  private RequestStatus requestStatus;

  @Column(name = "create_date")
  private LocalDateTime createDate;

  @Column(name = "latest_date")
  private LocalDateTime latestDate;

  @Column(name = "duration")
  private LocalDateTime duration;

  @Column(name = "approver")
  private String approver;

  @Column(name = "data")
  private String data;

  @OneToMany(mappedBy = "applicationsRequest", fetch = FetchType.LAZY)
  private List<ReviewRequest> reviewRequests;

  @Column(name = "is_bookmark")
  @Type(type = "boolean")
  private Boolean isBookmark;

  @Column(name = "is_remind")
  @Type(type = "boolean")
  private Boolean isRemind;
}