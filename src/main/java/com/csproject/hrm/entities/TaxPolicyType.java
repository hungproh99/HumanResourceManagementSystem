package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ETax;
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
@Table(name = "tax_policy_type")
public class TaxPolicyType {
  @Id
  @Column(name = "tax_policy_type_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "tax_policy_type")
  private ETax taxPolicyType;

  @Column(name = "tax_policy_name")
  private String taxPolicyName;

  @Column(name = "percentage")
  private int percentage;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "taxPolicyType", fetch = FetchType.LAZY)
  private TaxPolicy taxPolicy;

  @OneToOne(mappedBy = "taxPolicyType", fetch = FetchType.LAZY)
  private EmployeeTax employeeTax;
}
