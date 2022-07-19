package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EPolicyName;
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
@Table(name = "policy_name")
public class PolicyName {
  @Id
  @Column(name = "policy_name_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "policy_name")
  private EPolicyName ePolicyName;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = PolicyType.class)
  @JoinColumn(name = "policy_type_id")
  private PolicyType policyType;

  @OneToOne(mappedBy = "policyName", fetch = FetchType.LAZY)
  private EmployeeInsurance employeeInsurance;

  @OneToOne(mappedBy = "policyName", fetch = FetchType.LAZY)
  private EmployeeTax employeeTax;
}
