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
@Table(name = "contract_type")
public class ContractType {
    @Id
    @Column(name = "type_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "contractType", fetch = FetchType.LAZY)
    private WorkingHistory workingHistory;

    @OneToOne(mappedBy = "contractType", fetch = FetchType.LAZY)
    private WorkingContract workingContract;
}
