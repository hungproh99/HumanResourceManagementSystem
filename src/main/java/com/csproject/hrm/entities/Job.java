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
@Table(name = "job")
public class Job {
  @Id
  @Column(name = "job_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "grade")
  private String grade;

  @Column(name = "position")
  private String position;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "job", fetch = FetchType.LAZY)
  private WorkingContract workingContract;
}
