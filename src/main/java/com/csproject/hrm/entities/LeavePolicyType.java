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
@Table(name = "leave_policy_type")
public class LeavePolicyType {
  @Id
  @Column(name = "leave_policy_type_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "leave_policy_type")
  private EWorkingPolicyType workingPolicyType;

  @Column(name = "leave_policy_name")
  private String workingPolicyName;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "leavePolicyType", fetch = FetchType.LAZY)
  private LeavePolicy leavePolicy;
}
