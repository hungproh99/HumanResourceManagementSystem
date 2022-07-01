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
@Table(name = "compensation_benefit_policy")
public class CompensationBenefitPolicy {
  @Id
  @Column(name = "compensation_benefit_id")
  private String id;

  @Column(name = "created_date")
  private LocalDate startDate;

  @Column(name = "effective_date")
  private LocalDate endDate;

  @Column(name = "compensation_benefit_status")
  @Type(type = "boolean")
  private Boolean compensationBenefitStatus;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "compensation_benefit_policy_type_id")
  private CompensationBenefitPolicyType compensationBenefitPolicyType;
}
