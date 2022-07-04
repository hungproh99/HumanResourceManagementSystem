package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EGradeType;
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
@Table(name = "grade_type")
public class GradeType {
  @Id
  @Column(name = "grade_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private EGradeType eGradeType;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
  private List<WorkingPlace> workingPlace;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Job.class)
  @JoinColumn(name = "job_id")
  private Job job;
}
