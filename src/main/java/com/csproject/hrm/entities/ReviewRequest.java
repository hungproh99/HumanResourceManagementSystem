package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "review_request")
public class ReviewRequest {
  @Id
  @Column(name = "review_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "employee_id")
  private String employeeId;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = ApplicationsRequest.class)
  @JoinColumn(name = "applications_request_id")
  private ApplicationsRequest applicationsRequest;
}
