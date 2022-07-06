package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EDeduction;
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
@Table(name = "deduction_type")
public class DeductionType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "deduction_type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "deduction_type")
  private EDeduction eDeduction;

  @OneToMany(mappedBy = "deductionType", fetch = FetchType.LAZY)
  private List<DeductionSalary> deductionSalaries;
}
