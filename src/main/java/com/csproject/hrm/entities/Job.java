package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EJob;
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
@Table(name = "job")
public class Job {
  @Id
  @Column(name = "job_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "position")
  private EJob eJob;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
  private List<WorkingPlace> workingPlace;

  @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
  private List<GradeType> grades;
}
