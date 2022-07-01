package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "working_place")
public class WorkingPlace {
  @Id
  @Column(name = "working_place_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "area_id")
  private Area area;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "office_id")
  private Office office;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "job_id")
  private Job job;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "grade_id")
  private GradeType grade;

  @Column(name = "working_place_status")
  @Type(type = "boolean")
  private Boolean workingPlaceStatus;
}
