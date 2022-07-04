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
@Table(name = "leave_policy")
public class LeavePolicy {
  @Id
  @Column(name = "leave_policy_id")
  private String id;

  @Column(name = "created_date")
  private LocalDate startDate;

  @Column(name = "effective_date")
  private LocalDate endDate;

  @Column(name = "leave_policy_status")
  @Type(type = "boolean")
  private Boolean leavePolicyStatus;

  @Column(name = "leave_policy_name")
  private String workingPolicyName;

  @Column(name = "description")
  private String description;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = LeavePolicyType.class)
  @JoinColumn(name = "leave_policy_type_id")
  private LeavePolicyType leavePolicyType;
}
