package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EEmployeeAssetStatus;
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
@Table(name = "employee_asset")
public class EmployeeAsset {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "employee_asset_id")
  private Long id;

  @Column(name = "borrowDate")
  private LocalDate borrowDate;

  @Column(name = "returnDate")
  private LocalDate returnDate;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private EEmployeeAssetStatus status;

  @Column(name = "description")
  private String description;

  @Column(name = "quantity")
  private int quantity;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = SalaryContract.class)
  @JoinColumn(name = "category_asset_id")
  private CategoryCompanyAsset categoryCompanyAsset;
}
