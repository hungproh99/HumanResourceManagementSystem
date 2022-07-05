package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EPolicyType;
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
@Table(name = "policy_type")
public class PolicyType {
  @Id
  @Column(name = "policy_type_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "policy_type")
  private EPolicyType ePolicyType;

  @Column(name = "policy_name")
  private String policyName;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = PolicyCategory.class)
  @JoinColumn(name = "policy_category_id")
  private PolicyCategory policyCategory;

  @OneToMany(mappedBy = "policyType", fetch = FetchType.LAZY)
  private List<Policy> policy;

  @OneToOne(mappedBy = "policyType", fetch = FetchType.LAZY)
  private EmployeeInsuranceTax employeeInsuranceTax;
}
