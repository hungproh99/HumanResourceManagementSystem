package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "checkin_checkout")
public class CheckInCheckOut {
  @Id
  @Column(name = "checkin_checkout_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "timekeeping_id")
  private Timekeeping timekeeping;

  @Column(name = "checkin")
  private LocalDate checkin;

  @Column(name = "checkout")
  private LocalDate checkout;
}
