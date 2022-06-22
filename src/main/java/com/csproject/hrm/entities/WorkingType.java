package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EGradeType;
import com.csproject.hrm.common.enums.EWorkingType;
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
@Table(name = "working_type")
public class WorkingType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private EWorkingType eWorkingType;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "workingType", fetch = FetchType.LAZY)
  private Employee employee;
}
