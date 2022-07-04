package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

  @Column(name = "description")
  private String description;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = PolicyType.class)
  @JoinColumn(name = "policy_type_id")
  private PolicyType policyType;
}
