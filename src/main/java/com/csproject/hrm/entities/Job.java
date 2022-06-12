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
@Table(name = "job")
public class Job {
  @Id
  @Column(name = "job_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "position")
  private String position;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "job", fetch = FetchType.LAZY)
  private WorkingContract workingContract;

  @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
  private List<Grade> grades;
}