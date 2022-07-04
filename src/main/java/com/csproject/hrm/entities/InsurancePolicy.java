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
@Table(name = "insurance_policy")
public class InsurancePolicy {
  @Id
  @Column(name = "insurance_policy_id")
  private String id;

  @Column(name = "created_date")
  private LocalDate startDate;

  @Column(name = "effective_date")
  private LocalDate endDate;

  @Column(name = "insurance_policy_status")
  @Type(type = "boolean")
  private Boolean insurancePolicyStatus;

  @Column(name = "insurance_policy_name")
  private String insurancePolicyName;

  @Column(name = "percentage")
  private int percentage;

  @Column(name = "address")
  private String address;

  @Column(name = "description")
  private String description;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = InsurancePolicyType.class)
  @JoinColumn(name = "insurance_policy_type_id")
  private InsurancePolicyType insurancePolicyType;
}
