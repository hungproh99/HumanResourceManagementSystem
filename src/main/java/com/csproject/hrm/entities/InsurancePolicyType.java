package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EInsurance;
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
@Table(name = "insurance_policy_type")
public class InsurancePolicyType {
  @Id
  @Column(name = "insurance_policy_type_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "insurance_policy_type")
  private EInsurance insurancePolicyType;

  @Column(name = "insurance_policy_name")
  private String insurancePolicyName;

  @Column(name = "percentage")
  private int percentage;

  @Column(name = "address")
  private String address;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "insurancePolicyType", fetch = FetchType.LAZY)
  private InsurancePolicy insurancePolicy;

  @OneToOne(mappedBy = "insurancePolicyType", fetch = FetchType.LAZY)
  private EmployeeInsurance employeeInsurance;
}
