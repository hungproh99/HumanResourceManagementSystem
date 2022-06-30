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
@Table(name = "working_policy")
public class WorkingPolicy {
  @Id
  @Column(name = "working_policy_id")
  private String id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "working_policy_type_id")
  private WorkingPolicyType workingPolicyType;
}
