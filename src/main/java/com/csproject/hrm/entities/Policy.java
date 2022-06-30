package com.csproject.hrm.entities;

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
@Table(name = "policy")
public class Policy {
  @Id
  @Column(name = "policy_id")
  private String id;

  @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
  private List<PolicyType> policyTypes;
}
