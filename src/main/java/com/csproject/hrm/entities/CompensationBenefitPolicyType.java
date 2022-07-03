package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ECompensationBenefitPolicyType;
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
@Table(name = "compensation_benefit_policy_type")
public class CompensationBenefitPolicyType {
  @Id
  @Column(name = "compensation_benefit_policy_type_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "compensation_benefit_policy_type")
  private ECompensationBenefitPolicyType compensationBenefitPolicyType;

  @OneToMany(mappedBy = "compensationBenefitPolicyType", fetch = FetchType.LAZY)
  private List<CompensationBenefitPolicy> compensationBenefitPolicy;
}
