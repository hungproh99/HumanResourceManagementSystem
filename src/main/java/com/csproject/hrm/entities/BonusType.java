package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EBonus;
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
@Table(name = "bonus_type")
public class BonusType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bonus_type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "bonus_type")
  private EBonus eBonus;

  @OneToMany(mappedBy = "bonusType", fetch = FetchType.LAZY)
  private List<BonusSalary> bonusSalaries;
}
