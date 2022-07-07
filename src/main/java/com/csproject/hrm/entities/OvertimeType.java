package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EOvertime;
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
@Table(name = "overtime_type")
public class OvertimeType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "overtime_type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "overtime_type")
  private EOvertime eOvertime;

  @OneToOne(mappedBy = "overtimeType", fetch = FetchType.LAZY)
  private Overtime overTime;
}
