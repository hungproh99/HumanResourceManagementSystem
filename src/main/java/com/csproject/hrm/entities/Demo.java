package com.csproject.hrm.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "Demo")
public class Demo {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private Long demoID;

  @Column(name = "date", columnDefinition = "DATE")
  @NotNull
  private LocalDate Date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "demo2_id", referencedColumnName = "id")
  @ToString.Exclude
  private Demo2 demo;
}
