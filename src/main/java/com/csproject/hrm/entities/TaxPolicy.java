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
@Table(name = "tax_policy")
public class TaxPolicy {
  @Id
  @Column(name = "tax_policy_id")
  private String id;

  @Column(name = "created_date")
  private LocalDate startDate;

  @Column(name = "effective_date")
  private LocalDate endDate;

  @Column(name = "tax_policy_status")
  @Type(type = "boolean")
  private Boolean taxPolicyStatus;

  @Column(name = "tax_policy_name")
  private String taxPolicyName;

  @Column(name = "percentage")
  private int percentage;

  @Column(name = "description")
  private String description;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = TaxPolicyType.class)
  @JoinColumn(name = "tax_policy_type_id")
  private TaxPolicyType taxPolicyType;
}
