package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EPolicyCategory;
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
@Table(name = "policy_category")
public class PolicyCategory {
  @Id
  @Column(name = "policy_category_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "policy_category")
  private EPolicyCategory ePolicyCategory;

  @OneToMany(mappedBy = "policyCategory", fetch = FetchType.LAZY)
  private List<PolicyType> policy;
}
