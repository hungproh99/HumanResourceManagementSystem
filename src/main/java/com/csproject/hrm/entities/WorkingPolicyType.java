package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EWorkingPolicyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

  @OneToMany(mappedBy = "workingPolicyType", fetch = FetchType.LAZY)
  private List<WorkingPolicy> workingPolicy;
}
