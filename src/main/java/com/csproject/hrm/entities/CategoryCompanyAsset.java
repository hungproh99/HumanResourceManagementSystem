package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EAsset;
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
@Table(name = "category_company_asset")
public class CategoryCompanyAsset {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_asset_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "asset_name")
  private EAsset eAsset;

  @Column(name = "quantity")
  private int quantity;

  @OneToMany(mappedBy = "categoryCompanyAsset", fetch = FetchType.LAZY)
  private List<EmployeeAsset> employeeAssets;
}
