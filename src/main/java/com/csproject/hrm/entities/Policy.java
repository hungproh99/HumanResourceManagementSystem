package com.csproject.hrm.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "policy")
public class Policy {
  @Id
  @Column(name = "policy_id")
  private String id;

  @Column(name = "created_date")
  private LocalDate startDate;

  @Column(name = "effective_date")
  private LocalDate endDate;

  @Column(name = "policy_status")
  @Type(type = "boolean")
  private Boolean policyStatus;

  @Column(name = "description", length = 1000)
  private String description;

  @Column(name = "data")
  private String data;

  @Column(name = "maximum_level_accept")
  private int maximumLevelAccept;

  @Column(name = "data")
  private String data;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = PolicyType.class)
  @JoinColumn(name = "policy_type_id")
  private PolicyType policyType;

  @OneToOne(mappedBy = "policy", fetch = FetchType.LAZY)
  private RequestName requestName;
}