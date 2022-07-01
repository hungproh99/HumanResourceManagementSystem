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
@Table(name = "working_policy")
public class WorkingPolicy {
  @Id
  @Column(name = "working_policy_id")
  private String id;

  @Column(name = "created_date")
  private LocalDate startDate;

  @Column(name = "effective_date")
  private LocalDate endDate;

  @Column(name = "working_policy_status")
  @Type(type = "boolean")
  private Boolean workingPolicyStatus;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "working_policy_type_id")
  private WorkingPolicyType workingPolicyType;
}
