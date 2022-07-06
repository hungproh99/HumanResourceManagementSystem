package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "overtime")
public class Overtime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "overtime_id")
  private Long id;

  @Column(name = "startTime")
  private LocalTime startTime;

  @Column(name = "endTime")
  private LocalTime endTime;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "overtime_type_id")
  private OvertimeType overtimeType;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "timekeeping_id")
  private Timekeeping timekeeping;
}
