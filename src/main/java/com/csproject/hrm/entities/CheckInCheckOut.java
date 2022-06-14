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
@Table(name = "checkin_checkout")
public class CheckInCheckOut {
  @Id
  @Column(name = "checkin_checkout_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Timekeeping.class)
  @JoinColumn(name = "timekeeping_id")
  private Timekeeping timekeeping;

  @Column(name = "checkin")
  private LocalTime checkin;

  @Column(name = "checkout")
  private LocalTime checkout;
}
