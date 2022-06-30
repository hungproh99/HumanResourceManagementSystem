package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EWorkingPolicyType;
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
@Table(name = "working_policy_type")
public class WorkingPolicyType {
  @Id
  @Column(name = "working_policy_type_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "working_policy_type")
  private EWorkingPolicyType workingPolicyType;

  @Column(name = "working_policy_name")
  private String workingPolicyName;

  @OneToOne(mappedBy = "workingPolicyType", fetch = FetchType.LAZY)
  private WorkingPolicy workingPolicy;
}
