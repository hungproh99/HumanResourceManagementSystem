package com.csproject.hrm.entities;

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
@Table(name = "grade")
public class Grade {
  @Id
  @Column(name = "grade_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String grade;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "grade", fetch = FetchType.LAZY)
  private WorkingContract workingContract;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Job.class)
  @JoinColumn(name = "job_id")
  private Job job;
}
